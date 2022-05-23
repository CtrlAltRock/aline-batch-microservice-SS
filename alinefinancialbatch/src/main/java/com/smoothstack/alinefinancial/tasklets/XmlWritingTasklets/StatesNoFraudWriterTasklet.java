package com.smoothstack.alinefinancial.tasklets.XmlWritingTasklets;

import com.smoothstack.alinefinancial.dto.StatesNoFraud;
import com.smoothstack.alinefinancial.enums.XmlFile;
import com.smoothstack.alinefinancial.maps.AnalysisMap;
import com.smoothstack.alinefinancial.models.State;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;

import static java.util.Objects.isNull;

@Slf4j(topic = "StatesNoFraudWriterTasklet")
public class StatesNoFraudWriterTasklet implements Tasklet {

    private AnalysisMap analysisMap = AnalysisMap.getAnalysisMap();

    private String filePath = XmlFile.FILEPATH.toString();

    private String fileName = XmlFile.STATESNOFRAUD.toString();

    public StatesNoFraudWriterTasklet(String filePath, String fileName) {
        try {
            if(new File(filePath).isDirectory()) {
                this.filePath = filePath;
                this.fileName = fileName;
            } else {
                throw new IllegalArgumentException("filePath is not a directory");
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        try {
            if(!isNull(analysisMap.getReportMap().get("states-no-fraud"))) {
                XStream stream = new XStream();
                stream.alias("StatesNoFraud", StatesNoFraud.class);
                stream.alias("State", State.class);
                FileWriter writer = new FileWriter(Path.of(filePath, fileName).toString());
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(stream.toXML(analysisMap.getReportMap().get("states-no-fraud")));
                writer.append(stringBuilder);
                writer.close();
            } else {
                log.info("states-no-fraud not found in report");
            }
        } catch (Exception e) {
            log.error(e.toString());
        }

        return null;
    }
}
