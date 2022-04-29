package com.smoothstack.alinefinancial.Tasklets;

import com.smoothstack.alinefinancial.Caches.CardCache;
import com.smoothstack.alinefinancial.Models.Card;
import com.thoughtworks.xstream.XStream;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.FileWriter;

public class XmlCardWriterTasklet implements Tasklet {

    private final CardCache cardCache = CardCache.getInstance();

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        XStream cardXStream = new XStream();
        cardXStream.alias("card", Card.class);
        FileWriter cardFileWriter = new FileWriter("src/main/ProcessedOutFiles/XmlCards.xml");
        StringBuilder cardStringBuilder = new StringBuilder();
        cardStringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        cardStringBuilder.append("<Cards>");
        cardCache.getGeneratedCards().forEach((k, v) -> {
            for (Card card : v) {
                cardStringBuilder.append(cardXStream.toXML(card));
            }
        });
        cardStringBuilder.append("</Cards>");
        cardFileWriter.append(cardStringBuilder);
        cardFileWriter.close();
        return null;
    }
}
