package com.smoothstack.alinefinancial.tasklets.XmlWritingTasklets;


import com.smoothstack.alinefinancial.xmlmodels.InsufficientBalance;
import com.smoothstack.alinefinancial.enums.XmlFile;
import com.smoothstack.alinefinancial.maps.AnalysisMap;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.FileWriter;

@Slf4j(topic = "XmlInsufficientBalanceUserTasklet")
public class XmlInsufficientBalanceUserTasklet implements Tasklet {

    private final AnalysisMap analysis = AnalysisMap.getAnalysisMap();

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        try {
            XStream stream = new XStream();
            stream.alias("InsufficientBalance", InsufficientBalance.class);
            FileWriter insufficientBalanceFile = new FileWriter("src/main/ProcessedOutFiles/XmlUsersInsufficientBalance.xml");
            StringBuilder fileBuilder = new StringBuilder();
            fileBuilder.append(XmlFile.HEADER.toString());
            fileBuilder.append(stream.toXML(analysis.getReportMap().get("insufficient-balance")));
            insufficientBalanceFile.append(fileBuilder);
            insufficientBalanceFile.close();

        } catch (Exception e) {
            log.error(e.toString());
        }

        return null;

    }
}
