package com.smoothstack.alinefinancial.Tasklets;

import com.smoothstack.alinefinancial.Caches.*;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class AnalysisTasklet implements Tasklet {

    private CardCache cardCache = CardCache.getInstance();
    private MerchantCache merchantCache = MerchantCache.getInstance();
    private StateCache stateCache = StateCache.getInstance();
    private UserCache userCache = UserCache.getInstance();
    private AnalysisMap report = AnalysisMap.getInstance();


    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        report.setStatistic("total-unique-merchants", merchantCache.getGeneratedMerchants().size());
        /*stateCache.getSeenStates().forEach((k, v) -> {
            System.out.println(v.toString());
        });*/
        return null;
    }
}
