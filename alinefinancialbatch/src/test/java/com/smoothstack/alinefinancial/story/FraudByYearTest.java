package com.smoothstack.alinefinancial.story;

import com.smoothstack.alinefinancial.maps.AnalysisMap;
import com.smoothstack.alinefinancial.models.Transaction;
import com.smoothstack.alinefinancial.processors.AnalysisProcessor;
import com.smoothstack.alinefinancial.tasklets.AnalysisTasklet;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class FraudByYearTest {

    @Autowired
    private FlatFileItemReader<Transaction> reader;

    private AnalysisProcessor analysisProcessor = new AnalysisProcessor();

    private AnalysisMap analysisMap = AnalysisMap.getInstance();

    private AnalysisTasklet analysisTasklet = new AnalysisTasklet();


    @Test
    public void getTransactionsAndTestInsufficientBalance() throws Exception {
        // given
        ExecutionContext ctx = new ExecutionContext();
        ctx.put("fileName", "src/test/files/fraud.csv");
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(ctx);

        // when
        List<Transaction> transactions = StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            List<Transaction> result = new ArrayList<>();
            Transaction item;
            reader.open(stepExecution.getExecutionContext());
            while((item = reader.read()) != null) {
                result.add(item);
            }
            reader.close();
            return result;
        });

        // size is an int no equals method
        assertEquals(transactions.size(), 22);

        transactions.forEach( transaction -> {
            try {
                analysisProcessor.process(transaction);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        // run the analysis tasklet
        analysisTasklet.execute(null, null);

        // getting fraudulent transactions and total transactions
        HashMap<Integer, Long> fraudByYearMap = analysisMap.getFraudByYearMap();
        HashMap<Integer, Long> transactionsByYearMap = analysisMap.getTransactionsByYearMap();

        // there are 11 years in total
        assertEquals(transactionsByYearMap.size(), 11);
        assertEquals(fraudByYearMap.size(), 11);

        assertEquals(analysisMap.getReportMap().get("fraud-year-2002"), (1./6.) * 100.00);
        assertEquals(analysisMap.getReportMap().get("fraud-year-2003"), (2./3.) * 100.00);
        assertEquals(analysisMap.getReportMap().get("fraud-year-2004"), (0./2.) * 100.00);
        assertEquals(analysisMap.getReportMap().get("fraud-year-2005"), (0./1.) * 100.00);
        assertEquals(analysisMap.getReportMap().get("fraud-year-2006"), (1./2.) * 100.00);
        assertEquals(analysisMap.getReportMap().get("fraud-year-2007"), (1./1.) * 100.00);
        assertEquals(analysisMap.getReportMap().get("fraud-year-2008"), (0./1.) * 100.00);
        assertEquals(analysisMap.getReportMap().get("fraud-year-2009"), (0./2.) * 100.00);
        assertEquals(analysisMap.getReportMap().get("fraud-year-2010"), (0./2.) * 100.00);
        assertEquals(analysisMap.getReportMap().get("fraud-year-2011"), (1./1.) * 100.00);
        assertEquals(analysisMap.getReportMap().get("fraud-year-2012"), (1./1.) * 100.00);


    }


    @Configuration
    @EnableBatchProcessing
    static class MyConfiguration {

        @Bean
        @StepScope
        public FlatFileItemReader<Transaction> reader(@Value("#{stepExecutionContext['fileName']}") String filename) {
            return new FlatFileItemReaderBuilder<Transaction>()
                    .name("csvReader")
                    .resource(new FileSystemResource(filename))
                    .linesToSkip(1)
                    .delimited()
                    .names("user", "card", "year", "month", "day", "time", "amount", "method", "merchant_name", "merchant_city", "merchant_state", "merchant_zip", "mcc", "errors", "fraud")
                    .fieldSetMapper(new BeanWrapperFieldSetMapper<Transaction>() {{
                        setTargetType(Transaction.class);
                    }})
                    .build();
        }
    }
}
