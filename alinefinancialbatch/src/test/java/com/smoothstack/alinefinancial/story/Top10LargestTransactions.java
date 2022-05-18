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
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class Top10LargestTransactions {

    @Autowired
    private FlatFileItemReader<Transaction> reader;

    private AnalysisProcessor analysisProcessor = new AnalysisProcessor();

    private AnalysisMap analysisMap = AnalysisMap.getAnalysisMap();

    private AnalysisTasklet analysisTasklet = new AnalysisTasklet();


    @Test
    public void testTop10LargestTransactions() throws Exception {
        // given
        ExecutionContext ctx = new ExecutionContext();
        ctx.put("fileName", "src/test/files/top_10_largest.csv");
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

        // size is an int "No" equals method
        assertEquals(transactions.size(), 22);

        // count insufficient balances
        transactions.forEach((item) -> {
            try {
                analysisProcessor.process(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Transaction t1 = new Transaction(1l, 0l, 2002, 9, 1, "06:21", "$1544.09", "Swipe Transaction", "3527213246127876953", "La Verne", "CA", "91750.0", "5300", "Insufficient Balance,", "No");
        Transaction t2 = new Transaction(0l, 0l, 2002, 9, 1, "06:21", "$134.09", "Swipe Transaction", "3527213246127876953", "La Verne", "CA", "91750.0", "5300", "Insufficient Balance,", "No");
        Transaction t3 = new Transaction(0l, 0l, 2002, 9, 1, "06:21", "$134.09", "Swipe Transaction", "3527213246127876953", "La Verne", "CA", "91750.0", "5300", "Insufficient Balance,", "No");
        Transaction t4 = new Transaction(0l, 0l, 2002, 9, 1, "06:21", "$134.09", "Swipe Transaction", "3527213246127876953", "La Verne", "CA", "91750.0", "5300", "Insufficient Balance,", "Yes");
        Transaction t5 = new Transaction(3l, 0l, 2002, 9, 1, "06:21", "$121.09", "Swipe Transaction", "3527213246127876953", "La Verne", "CA", "91750.0", "5300", "Insufficient Balance,", "No");
        Transaction t6 = new Transaction(0l, 0l, 2002, 9, 1, "06:21", "$80.09", "Swipe Transaction", "3527213246127876953", "La Verne", "CA", "91750.0", "5300", "Insufficient Balance,", "No");
        Transaction t7 = new Transaction(2l, 0l, 2002, 9, 1, "06:21", "$80.09", "Swipe Transaction", "3527213246127876953", "La Verne", "CA", "91750.0", "5300", "Insufficient Balance,", "No");
        Transaction t8 = new Transaction(1l, 0l, 2002, 9, 1, "06:21", "$70.09", "Swipe Transaction", "3527213246127876953", "La Verne", "CA", "91750.0", "5300", "Insufficient Balance,", "No");
        Transaction t9 = new Transaction(1l, 0l, 2002, 9, 1, "06:21", "$70.09", "Swipe Transaction", "3527213246127876953", "La Verne", "CA", "91750.0", "5300", "Insufficient Balance,", "No");
        Transaction t10 = new Transaction(2l, 0l, 2002, 9, 1, "06:21", "$70.09", "Swipe Transaction", "3527213246127876953", "La Verne", "CA", "91750.0", "5300", "Insufficient Balance,", "No");


        List<Transaction> testLargestTransactions = Arrays.asList(t1, t2, t3, t4, t5,
                                                                  t6, t7, t8, t9, t10);

        assertEquals(analysisMap.getLargestTransactions().size(), 10);

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
