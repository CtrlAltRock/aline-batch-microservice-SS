package com.smoothstack.alinefinancial.tasklets.XmlWritingTasklets;

import com.smoothstack.alinefinancial.enums.XmlFile;
import com.smoothstack.alinefinancial.maps.AnalysisMap;
import com.smoothstack.alinefinancial.dto.ZipTransVol;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class TopFiveZipTransVol implements Tasklet {

    // default filePath
    private String filePath = XmlFile.FILEPATH.toString();

    // default fileName
    private String fileName = XmlFile.TOPZIPTRANSVOL.toString();

    private AnalysisMap analysisMap = AnalysisMap.getAnalysisMap();

    public TopFiveZipTransVol(String filePath, String fileName) {
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
            com.smoothstack.alinefinancial.dto.TopFiveZipTransVol topFive = new com.smoothstack.alinefinancial.dto.TopFiveZipTransVol();
            List<ZipTransVol> list = new ArrayList<>();
            for (Map.Entry<String, Long> entry : (List<Map.Entry<String, Long>>) analysisMap.getReportMap().get("top-five-zip-total-transactions")) {
                list.add(new ZipTransVol(entry.getKey(), entry.getValue()));
            }
            topFive.setZipTransVolList(list);

            XStream stream = new XStream();
            stream.alias("TopFiveZipTransVol", com.smoothstack.alinefinancial.dto.TopFiveZipTransVol.class);
            stream.alias("ZipTransVol", ZipTransVol.class);
            FileWriter writer = new FileWriter(Path.of(filePath, fileName).toString());
            writer.append(XmlFile.HEADER.toString());
            writer.append(stream.toXML(topFive));
            writer.close();

        } catch (Exception e) {
            log.error(e.toString());
        }

        return null;
    }
}
