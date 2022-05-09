package com.smoothstack.alinefinancial.Tasklets;

import com.smoothstack.alinefinancial.Maps.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j(topic = "AnalysisTasklet")
public class AnalysisTasklet implements Tasklet {

    private CardMap cardMap = CardMap.getInstance();
    private MerchantMap merchantMap = MerchantMap.getInstance();
    private StateMap stateMap = StateMap.getInstance();
    private UserMap userMap = UserMap.getInstance();
    private AnalysisMap report = AnalysisMap.getInstance();

    private Long numberOfUsersWithInsufficientBalanceAtLeastOnce = 0L;
    private Long numberOfUsersWithInsufficientBalanceMoreThanOnce = 0L;


    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {

            // capturing number of users
            Long numberOfUsers = Long.valueOf(userMap.getGeneratedUsers().size());
            report.setStatistic("number-of-users", numberOfUsers);

            // NRVNA - 87 - number of unique merchants
            report.setStatistic("total-unique-merchants", merchantMap.getGeneratedMerchants().size());

            // NRVNA - 84 & 85 - number of users with insufficient balance at least once and more than once
            userMap.getGeneratedUsers().forEach((k, v) -> {
                if(v.getInsufficientBalanceTransactions() > 1) {
                    numberOfUsersWithInsufficientBalanceMoreThanOnce += 1;
                }
                if(v.getInsufficientBalanceTransactions() >= 1) {
                    numberOfUsersWithInsufficientBalanceAtLeastOnce += 1;
                }
            });

            // calculating percentage
            Double percentageMoreThanOnce = numberOfUsersWithInsufficientBalanceMoreThanOnce.doubleValue() / numberOfUsers.doubleValue();
            Double percentageAtLeastOnce = numberOfUsersWithInsufficientBalanceAtLeastOnce.doubleValue() / numberOfUsers.doubleValue();

            report.setStatistic("number-of-users-with-insufficient-balance-at-least-once", numberOfUsersWithInsufficientBalanceAtLeastOnce);
            report.setStatistic("number-of-users-with-insufficient-balance-more-than-once", numberOfUsersWithInsufficientBalanceMoreThanOnce);

            report.setStatistic("percentage-of-users-with-insufficient-balance-at-least-once", percentageAtLeastOnce);
            report.setStatistic("percentage-of-users-with-insufficient-balance-more-than-once", percentageMoreThanOnce);

            // NRVNA-86 Identify top 5 recurring transactions grouped by merchant
            /*merchantMap.getGeneratedMerchants().forEach((k, v) -> {
                report.setStatistic(v.getName(), v.getTransactionsByAmt().entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .limit(5)
                        .collect(Collectors.toList()));
            });*/

            // NRVNA-88 Percentage of fraud by year
            report.getFraudByYearMap().forEach((k, v) -> {
                StringBuilder fraud = new StringBuilder();
                fraud.append("fraud-year-");
                fraud.append(k.toString());
                report.setStatistic(fraud.toString(), v.doubleValue() / report.getTransactionsByYearMap().get(k).doubleValue() * 100) ;
            });

            // NRVNA-89 top 5 zipcodes with highest total money amount of transactions
            List<Map.Entry<String, Double>> collect = report.getZipTransactions()
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(5)
                    .collect(Collectors.toList());

            report.setStatistic("top-five-zip-codes-with-highest-transaction", collect);

            // NRVNA-89 top 5 zipcodes with highest volume of transactions - this records just number of transactions
            List<Map.Entry<String, Long>> topZipTotals = report.getZipTotalTransactions()
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(5)
                    .collect(Collectors.toList());

            report.setStatistic("top-five-zip-total-transactions", topZipTotals);

            // NRVNA-90 top 5 cities with highest volume of transactions
            List<Map.Entry<String, Long>> topCitiesTotals = report.getCitiesTotalTransactions()
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(5)
                    .collect(Collectors.toList());

            report.setStatistic("top-five-cities-total-transactions", topCitiesTotals);

            // NRVNA-93 top largest transactions
            report.setStatistic("top-ten-largest-transactions", report.getLargestTransactions());

            // NRVNA-91 types of transactions volume
            report.getTypesOfTransactions().forEach((k, v) -> {
                report.setStatistic(k, v);
            });

            // NRVNA-94 states with no fraud
            report.getStatesNoFraud().forEach((k, v) -> {
                if(v.get(true) == null) {
                    StringBuilder noFraud = new StringBuilder();
                    noFraud.append("no-fraud-");
                    noFraud.append(k);
                    report.setStatistic(noFraud.toString(), "no-fraud");
                }
            });

            // NRVNA-92 group transactions by zip and online over $100 and after 8PM
            report.getTransAfter8Above100().forEach((k, v) -> {
                StringBuilder transactionsGroup = new StringBuilder();
                transactionsGroup.append("zipcode-");
                transactionsGroup.append(k);
                transactionsGroup.append("-with-transactions");
                report.setStatistic(transactionsGroup.toString(), v);
            });

        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }
}
