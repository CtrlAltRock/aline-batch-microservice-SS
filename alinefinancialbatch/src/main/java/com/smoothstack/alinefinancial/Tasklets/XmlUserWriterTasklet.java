package com.smoothstack.alinefinancial.Tasklets;

import com.smoothstack.alinefinancial.Caches.UserCache;
import com.smoothstack.alinefinancial.Models.User;
import com.thoughtworks.xstream.XStream;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.FileWriter;

public class XmlUserWriterTasklet implements Tasklet {

    private final UserCache userCache = UserCache.getInstance();

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        XStream userXStream = new XStream();
        userXStream.alias("user", User.class);
        FileWriter userFile = new FileWriter("src/main/ProcessedOutFiles/XmlUsers.xml");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        stringBuilder.append("<Users>\n");
        userCache.getGeneratedUsers().forEach((k, v) -> {
            stringBuilder.append(userXStream.toXML(v));
        });
        stringBuilder.append("</Users>");
        userFile.append(stringBuilder);
        userFile.close();
        return null;
    }
}
