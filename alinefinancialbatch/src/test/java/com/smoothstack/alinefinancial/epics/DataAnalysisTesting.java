package com.smoothstack.alinefinancial.epics;

import com.smoothstack.alinefinancial.dto.InsufficientBalance;
import com.smoothstack.alinefinancial.dto.RecurringTransaction;
import com.smoothstack.alinefinancial.dto.UniqueMerchants;
import com.smoothstack.alinefinancial.dto.UserDeposit;
import com.smoothstack.alinefinancial.enums.StatisticStrings;
import com.smoothstack.alinefinancial.enums.TransactionType;
import com.smoothstack.alinefinancial.maps.AnalysisMap;
import com.smoothstack.alinefinancial.maps.StateMap;
import com.smoothstack.alinefinancial.models.State;
import com.smoothstack.alinefinancial.models.Transaction;
import com.smoothstack.alinefinancial.processors.AnalysisProcessor;
import com.smoothstack.alinefinancial.processors.MerchantProcessor;
import com.smoothstack.alinefinancial.processors.StateProcessor;
import com.smoothstack.alinefinancial.processors.UserProcessor;
import com.smoothstack.alinefinancial.tasklets.AnalysisTasklet;
import org.junit.jupiter.api.AfterEach;
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

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DataAnalysisTesting {

    @Autowired
    private FlatFileItemReader<Transaction> reader;

    @AfterEach
    public void resetAnalysisMap() {
        AnalysisMap.resetAllMaps();
    }


    @Test
    public void fraudByYearTest() throws Exception {

        AnalysisProcessor analysisProcessor = new AnalysisProcessor();

        AnalysisMap analysisMap = AnalysisMap.getAnalysisMap();

        AnalysisTasklet analysisTasklet = new AnalysisTasklet();

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
        System.out.println(transactionsByYearMap);
        System.out.println(fraudByYearMap);

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


    @Test
    public void InsufficientBalanceTest() throws Exception {

        AnalysisMap analysisMap = AnalysisMap.getAnalysisMap();

        AnalysisTasklet analysisTasklet = new AnalysisTasklet();

        UserProcessor userProcessor = new UserProcessor();

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


    @Test
    public void testTop10LargestTransactions() throws Exception {

        AnalysisProcessor analysisProcessor = new AnalysisProcessor();

        AnalysisMap analysisMap = AnalysisMap.getAnalysisMap();

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


    @Test
    public void TypesOfTransactions() throws Exception {

        AnalysisProcessor analysisProcessor = new AnalysisProcessor();

        AnalysisMap analysisMap = AnalysisMap.getAnalysisMap();

        AnalysisTasklet analysisTasklet = new AnalysisTasklet();

        // given
        ExecutionContext ctx = new ExecutionContext();
        ctx.put("fileName", "src/test/files/types_of_transactions.csv");
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

        // getting the types
        HashMap<String, Long> typesOfTransactions = analysisMap.getTypesOfTransactions();

        assertEquals(Integer.valueOf(typesOfTransactions.size()), 3);
        assertEquals(typesOfTransactions.get(TransactionType.SWIPE.toString()), 9);
        assertEquals(typesOfTransactions.get(TransactionType.ONLINE.toString()), 6);
        assertEquals(typesOfTransactions.get(TransactionType.CHIP.toString()), 7);



    }


    @Test
    public void uniqueMerchantsTest() throws Exception {

        MerchantProcessor merchantProcessor = new MerchantProcessor();

        AnalysisMap analysisMap = AnalysisMap.getAnalysisMap();

        AnalysisTasklet analysisTasklet = new AnalysisTasklet();

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
                merchantProcessor.process(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //StepContribution and ChunkContext can be null as the analysis does not rely on them
        analysisTasklet.execute(null, null);

        UniqueMerchants uniqueMerchants = (UniqueMerchants) analysisMap.getReportMap().get("unique-merchants");

        assertEquals(uniqueMerchants.getTotalUniqueMerchants(), 22);
    }


    @Test
    public void userDepositsTest() throws Exception {

        UserProcessor userProcessor = new UserProcessor();

        AnalysisMap analysisMap = AnalysisMap.getAnalysisMap();

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
        userDeposit.forEach((k, v) -> {
            System.out.println(v);
        });
        assertEquals(userDeposit.size(), 4);

        assertEquals(userDeposit.get(0L).getDeposits().size(), 2);
        assertEquals(userDeposit.get(1L).getDeposits().size(), 2);
        assertEquals(userDeposit.get(2L).getDeposits().size(), 1);
        assertEquals(userDeposit.get(3L).getDeposits().size(), 4);
    }


    @Test
    public void topFiveZipTransVolTest() throws Exception {
        AnalysisProcessor analysisProcessor = new AnalysisProcessor();

        AnalysisMap analysisMap = AnalysisMap.getAnalysisMap();

        AnalysisTasklet analysisTasklet = new AnalysisTasklet();

        // given
        ExecutionContext ctx = new ExecutionContext();
        ctx.put("fileName", "src/test/files/total_zip_volume.csv");
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
        assertEquals(transactions.size(), 100);

        transactions.forEach( transaction -> {
            try {
                analysisProcessor.process(transaction);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        // run the analysis tasklet
        analysisTasklet.execute(null, null);

        // make my list to check against analysis
        Map.Entry<String, Long> entry1 = new AbstractMap.SimpleEntry<String, Long>("91754.0", 19L);
        Map.Entry<String, Long> entry2 = new AbstractMap.SimpleEntry<String, Long>("91758.0", 18L);
        Map.Entry<String, Long> entry3 = new AbstractMap.SimpleEntry<String, Long>("91753.0", 17L);
        Map.Entry<String, Long> entry4 = new AbstractMap.SimpleEntry<String, Long>("91756.0", 14L);
        Map.Entry<String, Long> entry5 = new AbstractMap.SimpleEntry<String, Long>("91752.0", 12L);

        List<Map.Entry<String, Long>> myList = Arrays.asList(entry1, entry2, entry3, entry4, entry5);


        List<Map.Entry<String, Long>> list = (List<Map.Entry<String, Long>>) analysisMap.getReportMap().get("top-five-zip-total-transactions");
        assertEquals(list.size(), 5);

        assertEquals(list, myList);



    }

    @Test
    public void recurringTransactionsTest() throws Exception {
        MerchantProcessor merchantProcessor = new MerchantProcessor();

        AnalysisMap analysisMap = AnalysisMap.getAnalysisMap();

        AnalysisTasklet analysisTasklet = new AnalysisTasklet();

        // given
        ExecutionContext ctx = new ExecutionContext();
        ctx.put("fileName", "src/test/files/recurring_merchant.csv");
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


        // process recurring merchants
        transactions.forEach((item) -> {
            try {
                merchantProcessor.process(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //StepContribution and ChunkContext can be null as the analysis does not rely on them
        analysisTasklet.execute(null, null);

        List<Map.Entry<RecurringTransaction, Long>> recurringTransactions = (List<Map.Entry<RecurringTransaction, Long>>) analysisMap.getReportMap().get("recurring-transactions");

        assertEquals(recurringTransactions.size(), 5);
        List<Map.Entry<RecurringTransaction, Long>> list = Arrays.asList(new AbstractMap.SimpleEntry<>(
                new RecurringTransaction("9", BigDecimal.valueOf(-544.09), 1L, 0L), 17L),
                new AbstractMap.SimpleEntry<>(new RecurringTransaction("7", BigDecimal.valueOf(70.09), 1L, 0L), 16L),
                new AbstractMap.SimpleEntry<>(new RecurringTransaction("8", BigDecimal.valueOf(50.09), 1L, 0L), 15L),
                new AbstractMap.SimpleEntry<>(new RecurringTransaction("6", BigDecimal.valueOf(80.09), 0L, 0L), 14L),
                new AbstractMap.SimpleEntry<>(new RecurringTransaction("15", BigDecimal.valueOf(70.09), 2L, 0L), 13L)
        );

        assertEquals(recurringTransactions, list);



    }

    @Test
    public void TopFiveCitiesHighestVolTest() throws Exception {
        AnalysisProcessor analysisProcessor = new AnalysisProcessor();

        AnalysisMap analysisMap = AnalysisMap.getAnalysisMap();

        AnalysisTasklet analysisTasklet = new AnalysisTasklet();

        // given
        ExecutionContext ctx = new ExecutionContext();
        ctx.put("fileName", "src/test/files/cities_volume.csv");
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


        // process recurring merchants
        transactions.forEach((item) -> {
            try {
                analysisProcessor.process(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //StepContribution and ChunkContext can be null as the analysis does not rely on them
        analysisTasklet.execute(null, null);

        List<Map.Entry<String, Long>> topCitiesTotals = (List< Map.Entry<String, Long>>) analysisMap.getReportMap().get(StatisticStrings.TOPFIVECITIESTRANSACTIONS.toString());

        assertEquals(topCitiesTotals.size(), 5);

        Map.Entry<String, Long> city1 = topCitiesTotals.get(0);
        Map.Entry<String, Long> city2 = topCitiesTotals.get(1);
        Map.Entry<String, Long> city3 = topCitiesTotals.get(2);
        Map.Entry<String, Long> city4 = topCitiesTotals.get(3);
        Map.Entry<String, Long> city5 = topCitiesTotals.get(4);

        assertEquals(city1.getKey(), "Monterey Park");
        assertEquals(city1.getValue(), 20L);

        assertEquals(city2.getKey(), "Mira Loma");
        assertEquals(city2.getValue(), 16L);

        assertEquals(city3.getKey(), "La Verne");
        assertEquals(city3.getValue(), 12L);

        assertEquals(city4.getKey(), "Jefferson City");
        assertEquals(city4.getValue(), 8L);

        assertEquals(city5.getKey(), "Waynesville");
        assertEquals(city5.getValue(), 4L);

    }

    @Test
    public void StatesWithNoFraudTest() throws Exception {
        AnalysisProcessor analysisProcessor = new AnalysisProcessor();

        StateProcessor stateProcessor = new StateProcessor();

        StateMap stateMap = new StateMap();

        AnalysisMap analysisMap = AnalysisMap.getAnalysisMap();

        AnalysisTasklet analysisTasklet = new AnalysisTasklet();

        // given
        ExecutionContext ctx = new ExecutionContext();
        ctx.put("fileName", "src/test/files/states_no_fraud.csv");
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


        // process recurring merchants
        transactions.forEach((item) -> {
            try {
                stateProcessor.process(item);
                analysisProcessor.process(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //StepContribution and ChunkContext can be null as the analysis does not rely on them
        analysisTasklet.execute(null, null);

        List<State> statesNoFraud = (List<State>) analysisMap.getReportMap().get("states-no-fraud");
        assertEquals(statesNoFraud.size(), 3);

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
