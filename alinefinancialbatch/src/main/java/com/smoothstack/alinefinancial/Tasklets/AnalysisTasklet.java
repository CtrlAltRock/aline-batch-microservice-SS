package com.smoothstack.alinefinancial.Tasklets;

import com.smoothstack.alinefinancial.Maps.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;


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
        Long numberOfUsers = Long.valueOf(userMap.getGeneratedUsers().size());


        try {
            //NRVNA - 87 - number of unique merchants
            report.setStatistic("total-unique-merchants", merchantMap.getGeneratedMerchants().size());

            //NRVNA - 84 & 85 -
            userMap.getGeneratedUsers().forEach((k, v) -> {
                if(v.getInsufficientBalanceTransactions() > 1) {
                    numberOfUsersWithInsufficientBalanceMoreThanOnce += 1;
                }
                if(v.getInsufficientBalanceTransactions() >= 1) {
                    numberOfUsersWithInsufficientBalanceAtLeastOnce += 1;
                }
            });

            Double percentageMoreThanOnce = numberOfUsersWithInsufficientBalanceMoreThanOnce.doubleValue() / numberOfUsers.doubleValue();
            Double percentageAtLeastOnce = numberOfUsersWithInsufficientBalanceAtLeastOnce.doubleValue() / numberOfUsers.doubleValue();

            report.setStatistic("number-of-users-with-insufficient-balance-at-least-once", numberOfUsersWithInsufficientBalanceAtLeastOnce);
            report.setStatistic("number-of-users-with-insufficient-balance-more-than-once", numberOfUsersWithInsufficientBalanceMoreThanOnce);

            report.setStatistic("percentage-of-users-with-insufficient-balance-at-least-once", percentageAtLeastOnce);
            report.setStatistic("percentage-of-users-with-insufficient-balance-more-than-once", percentageMoreThanOnce);

        } catch (Exception e) {
            log.info(e.toString());
        }
        return null;
    }
}
