package com.smoothstack.alinefinancial.tasklets;

import com.smoothstack.alinefinancial.maps.AnalysisMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j(topic = "ReportTasklet")
public class ReportTasklet implements Tasklet {

    private AnalysisMap analysis = AnalysisMap.getAnalysisMap();

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        try {
            Long total =
            analysis.getMonthsOnlineTransactionsCount().values().stream().collect(Collectors.summingLong(Long::longValue));
            System.out.println(total);
            List<Map.Entry<Integer, Long>> thing1 = (List<Map.Entry<Integer, Long>> ) analysis.getReportMap().get("bottom-five-months-online-transactions");
            thing1.stream().forEach(x->System.out.println(x));
            /*List<Map.Entry<String, ConcurrentLinkedQueue<String>>> thing =  (List<Map.Entry<String, ConcurrentLinkedQueue<String>>>) analysis.getReportMap().get("city-merchants-online-count");
            thing.stream().forEach(x->System.out.println(x));*/
            /*analysis.getReportMap().forEach((k, v) -> {
                log.info(String.format("%s: %s", k, v));
            });*/
        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }
}
