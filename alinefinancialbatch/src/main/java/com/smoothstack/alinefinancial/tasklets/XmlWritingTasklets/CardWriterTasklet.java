package com.smoothstack.alinefinancial.tasklets.XmlWritingTasklets;

import com.smoothstack.alinefinancial.enums.XmlFile;
import com.smoothstack.alinefinancial.maps.CardMap;
import com.smoothstack.alinefinancial.models.Card;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;

@Slf4j(topic = "XmlCardWriterTasklet")
public class CardWriterTasklet implements Tasklet {

    // default filePath
    private String filePath = XmlFile.FILEPATH.toString();

    // default fileName
    private String fileName = "Cards.xml";

    private final CardMap cardMap = CardMap.getInstance();

    public CardWriterTasklet(String filePath, String fileName) {
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
            XStream cardXStream = new XStream();
            cardXStream.alias("Card", Card.class);
            FileWriter cardFileWriter = new FileWriter(Path.of(filePath, fileName).toString());
            StringBuilder cardStringBuilder = new StringBuilder();
            cardStringBuilder.append(XmlFile.HEADER.toString());
            cardStringBuilder.append("<Cards>\n");
            cardMap.getGeneratedCards().forEach((k, v) -> {
                for (Card card : v) {
                    cardStringBuilder.append(cardXStream.toXML(card));
                }
            });
            cardStringBuilder.append("\n</Cards>");
            cardFileWriter.append(cardStringBuilder);
            cardFileWriter.close();
        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }
}
