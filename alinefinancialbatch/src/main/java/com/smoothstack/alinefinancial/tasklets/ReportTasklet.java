package com.smoothstack.alinefinancial.tasklets;

import com.smoothstack.alinefinancial.maps.AnalysisMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@Slf4j(topic = "ReportTasklet")
public class ReportTasklet implements Tasklet {

    private AnalysisMap analysis = AnalysisMap.getAnalysisMap();

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        try {

            /*analysis.getReportMap().forEach((k, v) -> {
                log.info(String.format("%s: %s\n", k, v));
            });*/
        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }
}
