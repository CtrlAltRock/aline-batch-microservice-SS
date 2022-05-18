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
import org.springframework.beans.factory.annotation.Value;

import java.io.FileWriter;

@Slf4j(topic = "XmlCardWriterTasklet")
public class XmlCardWriterTasklet implements Tasklet {

    @Value("${cards.file}")
    private String filePath;

    private final CardMap cardMap = CardMap.getInstance();

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            System.out.println(filePath);
            XStream cardXStream = new XStream();
            cardXStream.alias("Card", Card.class);
            FileWriter cardFileWriter = new FileWriter(filePath);
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
