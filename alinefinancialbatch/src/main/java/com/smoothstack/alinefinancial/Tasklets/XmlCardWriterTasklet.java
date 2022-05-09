package com.smoothstack.alinefinancial.Tasklets;

import com.smoothstack.alinefinancial.Maps.CardMap;
import com.smoothstack.alinefinancial.Models.Card;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.FileWriter;

@Slf4j(topic = "XmlCardWriterTasklet")
public class XmlCardWriterTasklet implements Tasklet {

    private final CardMap cardMap = CardMap.getInstance();

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            XStream cardXStream = new XStream();
            cardXStream.alias("Card", Card.class);
            FileWriter cardFileWriter = new FileWriter("src/main/ProcessedOutFiles/XmlCards.xml");
            StringBuilder cardStringBuilder = new StringBuilder();
            cardStringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
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
