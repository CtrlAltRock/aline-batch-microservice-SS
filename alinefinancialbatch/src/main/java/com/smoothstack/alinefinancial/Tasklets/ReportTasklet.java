package com.smoothstack.alinefinancial.Tasklets;

import com.smoothstack.alinefinancial.Maps.AnalysisMap;
import com.smoothstack.alinefinancial.Models.Report;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Time;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@Slf4j(topic = "ReportTasklet")
public class ReportTasklet implements Tasklet {

    private AnalysisMap analysis = AnalysisMap.getInstance();

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            Report report = new Report();
            report.setTime(new Time().toString());
            report.setReport(analysis.getReportMap());
            analysis.getReportMap().forEach((k, v) -> {
                System.out.printf("%s: %s\n", k, v);
            });
        } catch (Exception e) {
            log.info(e.toString());
        }
        return null;
    }
}
