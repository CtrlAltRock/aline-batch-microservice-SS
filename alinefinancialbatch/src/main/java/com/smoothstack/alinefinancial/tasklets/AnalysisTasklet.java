package com.smoothstack.alinefinancial.tasklets;

import com.smoothstack.alinefinancial.analysismodels.InsufficientBalance;
import com.smoothstack.alinefinancial.analysismodels.UniqueMerchants;
import com.smoothstack.alinefinancial.maps.*;
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

    // maps for
    private CardMap cardMap = CardMap.getInstance();
    private MerchantMap merchantMap = MerchantMap.getInstance();
    private StateMap stateMap = StateMap.getInstance();
    private UserMap userMap = UserMap.getInstance();
    private AnalysisMap analysis = AnalysisMap.getInstance();

    private InsufficientBalance balances = new InsufficientBalance();

    private UniqueMerchants uniqueMerchants = new UniqueMerchants();

    private Long atLeastOnce = 0L;
    private Long moreThanOnce = 0L;


    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            // NRVNA - 87 - number of unique merchants
            uniqueMerchants.setTotalUniqueMerchants(Long.valueOf(merchantMap.getGeneratedMerchants().size()));
            analysis.setStatistic("unique-merchants", uniqueMerchants);

            // NRVNA - 84 & 85 - number of users with insufficient balance at least once and more than once

            // capturing number of users
            Long numberOfUsers = Long.valueOf(userMap.getGeneratedUsers().size());

            analysis.getInsufficientBalanceMap().forEach((k, v) -> {
                if(v >= 1) {
                    atLeastOnce += 1;
                }

                if(v > 1) {
                    moreThanOnce += 1;
                }
            });

            // setting InsufficientBalance properties
            balances.setNumberOfUsers(numberOfUsers);
            balances.setAtLeastOnce(atLeastOnce);
            balances.setMoreThanOnce(moreThanOnce);

            // calculating percentage
            balances.setPercentageAtLeastOnce(atLeastOnce.doubleValue() / numberOfUsers.doubleValue() * 100);
            balances.setPercentageMoreThanOnce(moreThanOnce.doubleValue() / numberOfUsers.doubleValue() * 100);

            //setting statistic
            analysis.setStatistic("insufficient-balance", balances);


            // NRVNA-88 Percentage of fraud by year
            analysis.getFraudByYearMap().forEach((k, v) -> {
                StringBuilder fraud = new StringBuilder();
                fraud.append("fraud-year-");
                fraud.append(k.toString());
                analysis.setStatistic(fraud.toString(), v.doubleValue() / analysis.getTransactionsByYearMap().get(k).doubleValue() * 100) ;
            });

            // NRVNA-89 top 5 zipcodes with highest total money amount of transactions
            List<Map.Entry<String, Double>> collect = analysis.getZipTransactions()
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(5)
                    .collect(Collectors.toList());

            analysis.setStatistic("top-five-zip-codes-with-highest-transaction", collect);

            // NRVNA-89 top 5 zipcodes with highest volume of transactions - this records just number of transactions
            List<Map.Entry<String, Long>> topZipTotals = analysis.getZipTotalTransactions()
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(5)
                    .collect(Collectors.toList());

            analysis.setStatistic("top-five-zip-total-transactions", topZipTotals);

            // NRVNA-90 top 5 cities with highest volume of transactions
            List<Map.Entry<String, Long>> topCitiesTotals = analysis.getCitiesTotalTransactions()
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(5)
                    .collect(Collectors.toList());

            analysis.setStatistic("top-five-cities-total-transactions", topCitiesTotals);

            // NRVNA-93 top largest transactions
            analysis.setStatistic("top-ten-largest-transactions", analysis.getLargestTransactions());

            // NRVNA-91 types of transactions volume
            analysis.getTypesOfTransactions().forEach((k, v) -> {
                analysis.setStatistic(k, v);
            });

            // NRVNA-94 states with no fraud
            analysis.getStatesNoFraud().forEach((k, v) -> {
                if(v.get(true) == null) {
                    StringBuilder noFraud = new StringBuilder();
                    noFraud.append("no-fraud-");
                    noFraud.append(k);
                    analysis.setStatistic(noFraud.toString(), "no-fraud");
                }
            });

            // NRVNA-92 group transactions by zip and online over $100 and after 8PM
            analysis.getTransAfter8Above100().forEach((k, v) -> {
                StringBuilder transactionsGroup = new StringBuilder();
                transactionsGroup.append("zipcode-");
                transactionsGroup.append(k);
                transactionsGroup.append("-with-transactions");
                analysis.setStatistic(transactionsGroup.toString(), v);
            });

        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }
}
