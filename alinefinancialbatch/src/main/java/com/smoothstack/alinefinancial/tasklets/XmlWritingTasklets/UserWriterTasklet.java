package com.smoothstack.alinefinancial.tasklets.XmlWritingTasklets;

import com.smoothstack.alinefinancial.enums.XmlFile;
import com.smoothstack.alinefinancial.maps.UserMap;
import com.smoothstack.alinefinancial.models.User;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;

@Slf4j(topic = "XmlUserWriterTasklet")
public class UserWriterTasklet implements Tasklet {

    // default filePath
    private String filePath = XmlFile.FILEPATH.toString();

    // default fileName
    private String fileName = XmlFile.USERS.toString();

    private final UserMap userMap = UserMap.getInstance();

    public UserWriterTasklet(String filePath, String fileName) {
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
            XStream userXStream = new XStream();
            userXStream.alias("User", User.class);
            userXStream.omitField(User.class, "cards");
            userXStream.omitField(User.class, "deposit");
            FileWriter userFile = new FileWriter(Path.of(filePath, fileName).toString());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(XmlFile.HEADER.toString());
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
