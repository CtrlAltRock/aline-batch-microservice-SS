package com.smoothstack.alinefinancial.tasklets.XmlWritingTasklets;

import com.smoothstack.alinefinancial.dto.MonthOnlineCount;
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

@Slf4j(topic = "LowestMonthOnlineTransactionCount")
public class LowestMonthOnlineTransactionCount implements Tasklet {

    // default filePath
    private String filePath = XmlFile.FILEPATH.toString();

    // default fileName
    private String fileName = XmlFile.MONTHONLINECOUNT.toString();

    private final AnalysisMap analysisMap = AnalysisMap.getAnalysisMap();

    public LowestMonthOnlineTransactionCount(String filePath, String fileName) {
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
            List<Map.Entry<Integer, Long>> lowestMonthsOfOnlineTransactions = (List<Map.Entry<Integer, Long>>) analysisMap.getReportMap().get("bottom-five-months-online-transactions");
            if(!isNull(lowestMonthsOfOnlineTransactions)) {
                XStream stream = new XStream();
                stream.alias("MonthOnlineCount", MonthOnlineCount.class);
                FileWriter writer = new FileWriter(Path.of(filePath, fileName).toString());
                StringBuilder fileBuilder = new StringBuilder();
                fileBuilder.append(XmlFile.HEADER.toString());

                lowestMonthsOfOnlineTransactions.stream().forEach( x -> {
                    fileBuilder.append(stream.toXML(new MonthOnlineCount(x.getKey(), x.getValue())));
                    fileBuilder.append("\n");
                });

                writer.append(fileBuilder);
                writer.close();
            }

        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }
}
