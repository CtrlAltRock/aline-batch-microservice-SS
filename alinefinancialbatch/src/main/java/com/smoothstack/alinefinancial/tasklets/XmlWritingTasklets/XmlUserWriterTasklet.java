package com.smoothstack.alinefinancial.tasklets.XmlWritingTasklets;

import com.smoothstack.alinefinancial.maps.UserMap;
import com.smoothstack.alinefinancial.models.User;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.FileWriter;

@Slf4j(topic = "XmlUserWriterTasklet")
public class XmlUserWriterTasklet implements Tasklet {

    private final UserMap userMap = UserMap.getInstance();

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            XStream userXStream = new XStream();
            userXStream.alias("User", User.class);
            userXStream.omitField(User.class, "cards");
            userXStream.omitField(User.class, "deposit");
            FileWriter userFile = new FileWriter("src/main/ProcessedOutFiles/XmlUsers.xml");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            stringBuilder.append("<Users>\n");
            userMap.getGeneratedUsers().forEach((k, v) -> {
                stringBuilder.append(userXStream.toXML(v));
            });
            stringBuilder.append("\n</Users>");
            userFile.append(stringBuilder);
            userFile.close();
        } catch(Exception e) {
            log.error(e.toString());
        }
        return null;
    }
}
