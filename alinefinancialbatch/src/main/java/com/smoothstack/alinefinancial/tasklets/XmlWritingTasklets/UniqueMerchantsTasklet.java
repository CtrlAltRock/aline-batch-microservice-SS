package com.smoothstack.alinefinancial.tasklets.XmlWritingTasklets;


import com.smoothstack.alinefinancial.dto.UniqueMerchants;
import com.smoothstack.alinefinancial.enums.XmlFile;
import com.smoothstack.alinefinancial.maps.AnalysisMap;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;

@Slf4j(topic = "XmlUniqueMerchantsTasklet")
public class UniqueMerchantsTasklet implements Tasklet {

    // default filePath
    private String filePath = XmlFile.FILEPATH.toString();

    // default fileName
    private String fileName = XmlFile.MERCHANTCOUNT.toString();

    private final AnalysisMap analysisMap = AnalysisMap.getAnalysisMap();

    public UniqueMerchantsTasklet(String filePath, String fileName) {
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
            XStream stream = new XStream();
            stream.alias("UniqueMerchants", UniqueMerchants.class);
            FileWriter fileWriter = new FileWriter(Path.of(filePath, fileName).toString());
            StringBuilder fileBuilder = new StringBuilder();
            fileBuilder.append(XmlFile.HEADER.toString());

            fileBuilder.append(stream.toXML(analysisMap.getReportMap().get("unique-merchants")));

            fileWriter.append(fileBuilder);
            fileWriter.close();

        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }


}
