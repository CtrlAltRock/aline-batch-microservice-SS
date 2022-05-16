package com.smoothstack.alinefinancial.story;


import com.smoothstack.alinefinancial.analysismodels.UserDeposit;
import com.smoothstack.alinefinancial.maps.AnalysisMap;
import com.smoothstack.alinefinancial.models.Transaction;
import com.smoothstack.alinefinancial.processors.UserProcessor;
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
public class UserDepositsTest {

    @Autowired
    private FlatFileItemReader<Transaction> reader;

    private UserProcessor userProcessor = new UserProcessor();

    private AnalysisMap analysisMap = AnalysisMap.getInstance();

    @Test
    public void userDepositsTest() throws Exception {
        // given
        ExecutionContext ctx = new ExecutionContext();
        ctx.put("fileName", "src/test/files/user_deposits.csv");
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


        // process 5 users' transactions
        transactions.forEach((item) -> {
            try {
                userProcessor.process(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // getting each user and their deposits
        HashMap<Long, UserDeposit> userDeposit = analysisMap.getUserDeposit();
        assertEquals(userDeposit.size(), 4);

        assertEquals(userDeposit.get(0L).getDeposits().size(), 2);
        assertEquals(userDeposit.get(1L).getDeposits().size(), 2);
        assertEquals(userDeposit.get(2L).getDeposits().size(), 1);
        assertEquals(userDeposit.get(3L).getDeposits().size(), 4);
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