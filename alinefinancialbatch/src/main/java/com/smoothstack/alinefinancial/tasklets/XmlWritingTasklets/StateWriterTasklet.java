package com.smoothstack.alinefinancial.tasklets.XmlWritingTasklets;

import com.smoothstack.alinefinancial.enums.XmlFile;
import com.smoothstack.alinefinancial.maps.StateMap;
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

@Slf4j(topic = "XmlStateWriterTasklet")
public class StateWriterTasklet implements Tasklet {

    private final StateMap stateMap = StateMap.getInstance();

    // default filePath
    private String filePath = XmlFile.FILEPATH.toString();

    // default fileName
    private String fileName = XmlFile.STATES.toString();


    public StateWriterTasklet(String filePath, String fileName) {
        try{
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
            XStream statesXStream = new XStream();
            statesXStream.alias("State", State.class);
            FileWriter statesFileWriter = new FileWriter(Path.of(filePath, fileName).toString());
            StringBuilder statesStringBuilder = new StringBuilder();
            statesStringBuilder.append(XmlFile.HEADER.toString());
            statesStringBuilder.append("<States>\n");
            stateMap.getInstance().getSeenStates().forEach((k, v) -> {
                if (v != null) statesStringBuilder.append(statesXStream.toXML(v));
            });
            statesStringBuilder.append("\n</States>");
            statesFileWriter.append(statesStringBuilder);
            statesFileWriter.close();
        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }
}
