package com.smoothstack.alinefinancial.tasklets.XmlWritingTasklets;

import com.smoothstack.alinefinancial.dto.CityAndTransactionVolume;
import com.smoothstack.alinefinancial.enums.StatisticStrings;
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
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

@Slf4j(topic = "CityAndTransactionVolWriterTasklet")
public class CityAndTransactionVolWriterTasklet implements Tasklet {

    private AnalysisMap analysisMap = AnalysisMap.getAnalysisMap();

    private String filePath = XmlFile.FILEPATH.toString();
    private String fileName = XmlFile.TOPFIVECITIESHIGHESTVOLUME.toString();

    public CityAndTransactionVolWriterTasklet(String filePath, String fileName) {
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
        try{
            if(!isNull(analysisMap.getReportMap().get(StatisticStrings.TOPFIVECITIESTRANSACTIONS.toString()))) {

                XStream stream = new XStream();
                stream.alias("CityAndTransactionVolume", CityAndTransactionVolume.class);
                FileWriter writer = new FileWriter(Path.of(filePath, fileName).toString());
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(XmlFile.HEADER.toString());
                stringBuilder.append("<TopFiveCityTransactionVolumes>\n");
                List<Map.Entry<String, Long>> topCitiesTotals = (List< Map.Entry<String, Long>>) analysisMap.getReportMap().get(StatisticStrings.TOPFIVECITIESTRANSACTIONS.toString());
                topCitiesTotals.forEach((o) -> {
                    stringBuilder.append(stream.toXML(new CityAndTransactionVolume(o.getKey(), o.getValue())));
                    stringBuilder.append("\n");
                });

                stringBuilder.append("\n</TopFiveCityTransactionVolumes>");
                writer.append(stringBuilder);
                writer.close();
            } else {
                log.info("No Report for Top Five Cities Transactions");
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }
}
