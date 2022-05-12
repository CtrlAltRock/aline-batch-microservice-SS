package com.smoothstack.alinefinancial.Tasklets.XmlWritingTasklets;


import com.smoothstack.alinefinancial.DataAnalysisModels.UniqueMerchants;
import com.smoothstack.alinefinancial.Maps.AnalysisMap;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.FileWriter;

@Slf4j(topic = "XmlUniqueMerchantsTasklet")
public class XmlUniqueMerchantsTasklet implements Tasklet {

    private final AnalysisMap analysisMap = AnalysisMap.getInstance();

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            XStream stream = new XStream();
            stream.alias("UniqueMerchants", UniqueMerchants.class);
            FileWriter fileWriter = new FileWriter("src/main/ProcessedOutFiles/XmlTotalUniqueMerchants.xml");
            StringBuilder fileBuilder = new StringBuilder();
            fileBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

            fileBuilder.append(stream.toXML(analysisMap.getReportMap().get("unique-merchants")));

            fileWriter.append(fileBuilder);
            fileWriter.close();

        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }


}
