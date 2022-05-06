package com.smoothstack.alinefinancial.Writers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.support.AbstractItemStreamItemWriter;

import java.util.List;

@Slf4j(topic = "ConsoleItemWriter")
public class ConsoleItemWriter extends AbstractItemStreamItemWriter {
    private Long chunksProcessed = 0L;
    @Override
    public void write(List items) throws Exception {
        StringBuilder infoMessage = new StringBuilder();
        chunksProcessed++;
        infoMessage.append("Chunks Processed: ");
        infoMessage.append(chunksProcessed);
        log.info(infoMessage.toString());
    }
}