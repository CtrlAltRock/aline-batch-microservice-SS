package com.smoothstack.alinefinancial.Tasklets;

import com.smoothstack.alinefinancial.Caches.MerchantCache;
import com.smoothstack.alinefinancial.Models.Merchant;
import com.thoughtworks.xstream.XStream;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.FileWriter;

public class XmlMerchantWriterTasklet implements Tasklet {

    private final MerchantCache merchantCache = MerchantCache.getInstance();

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        XStream merchantXStream = new XStream();
        merchantXStream.alias("merchant", Merchant.class);
        FileWriter merchantFileWriter = new FileWriter("src/main/ProcessedOutFiles/XmlMerchants.xml");
        StringBuilder merchantStringBuilder = new StringBuilder();
        merchantStringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        merchantStringBuilder.append("<Merchants>");

        merchantCache.getGeneratedMerchants().forEach((k, v) -> {
            merchantStringBuilder.append(merchantXStream.toXML(v));
        });
        merchantStringBuilder.append("</Merchants>");
        merchantFileWriter.append(merchantStringBuilder);
        merchantFileWriter.close();

        return null;
    }
}
