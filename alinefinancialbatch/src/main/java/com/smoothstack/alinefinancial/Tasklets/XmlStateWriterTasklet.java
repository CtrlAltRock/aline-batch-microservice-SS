package com.smoothstack.alinefinancial.Tasklets;

import com.smoothstack.alinefinancial.Caches.StateCache;
import com.smoothstack.alinefinancial.Models.State;
import com.thoughtworks.xstream.XStream;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import java.io.FileWriter;

public class XmlStateWriterTasklet implements Tasklet {

    private final StateCache stateCache = StateCache.getInstance();

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        XStream statesXStream = new XStream();
        statesXStream.alias("state", State.class);
        FileWriter statesFileWriter = new FileWriter("src/main/ProcessedOutFiles/XmlStates.xml");
        StringBuilder statesStringBuilder = new StringBuilder();
        statesStringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        statesStringBuilder.append("<States>");
        stateCache.getInstance().getSeenStates().forEach((k, v) -> {
            if (v != null) statesStringBuilder.append(statesXStream.toXML(v));
        });
        statesStringBuilder.append("</States>");
        statesFileWriter.append(statesStringBuilder);
        statesFileWriter.close();

        return null;
    }
}
