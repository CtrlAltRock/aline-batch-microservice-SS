package com.smoothstack.alinefinancial.story;

import com.smoothstack.alinefinancial.xmlmodels.InsufficientBalance;
import com.smoothstack.alinefinancial.maps.AnalysisMap;
import com.smoothstack.alinefinancial.models.Transaction;
import com.smoothstack.alinefinancial.processors.UserProcessor;
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
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ContextConfiguration(classes = InsufficientBalancesTest.MyConfiguration.class)
public class InsufficientBalancesTest {

        @Autowired
        private FlatFileItemReader<Transaction> reader;

        private UserProcessor userProcessor = new UserProcessor();

        private AnalysisMap analysisMap = AnalysisMap.getAnalysisMap();

        private AnalysisTasklet analysisTasklet = new AnalysisTasklet();


        @Test
        public void getTransactionsAndTestInsufficientBalance() throws Exception {
                // given
                ExecutionContext ctx = new ExecutionContext();
                ctx.put("fileName", "src/test/files/unique_merchants.csv");
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

                // count insufficient balances
                transactions.forEach((item) -> {
                        try {
                                userProcessor.process(item);
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                });

                // run the analysis tasklet
                analysisTasklet.execute(null, null);
                InsufficientBalance balances = (InsufficientBalance) analysisMap.getReportMap().get("insufficient-balance");
                assertEquals(balances.getNumberOfUsers(), 5);
                assertEquals(balances.getAtLeastOnce(), 5);
                assertEquals(balances.getMoreThanOnce(), 4);
                assertEquals(balances.getPercentageAtLeastOnce(), 100.00);
                assertEquals(balances.getPercentageMoreThanOnce(), 80.00);
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
