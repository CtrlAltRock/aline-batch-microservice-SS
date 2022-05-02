package com.smoothstack.alinefinancial.Tasklets;

import com.smoothstack.alinefinancial.Caches.AnalysisMap;
import com.smoothstack.alinefinancial.Caches.MerchantCache;
import com.smoothstack.alinefinancial.Models.Report;
import org.apache.tomcat.jni.Time;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class ReportTasklet implements Tasklet {

    private AnalysisMap analysis = AnalysisMap.getInstance();

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Report report = new Report();
        report.setTime(new Time().toString());
        report.setReport(analysis.getReportMap());
        analysis.getReportMap().forEach((k, v) -> {
            System.out.printf("%s: %s\n", k, v);
        });

        return null;
    }
}
