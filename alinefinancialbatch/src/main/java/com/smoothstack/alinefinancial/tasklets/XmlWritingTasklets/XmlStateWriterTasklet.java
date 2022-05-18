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
import java.io.FileWriter;

@Slf4j(topic = "XmlStateWriterTasklet")
public class XmlStateWriterTasklet implements Tasklet {

    private final StateMap stateMap = StateMap.getInstance();

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            XStream statesXStream = new XStream();
            statesXStream.alias("State", State.class);
            FileWriter statesFileWriter = new FileWriter("src/main/ProcessedOutFiles/XmlStates.xml");
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
