package com.smoothstack.alinefinancial.tasklets.XmlWritingTasklets;

import com.smoothstack.alinefinancial.enums.XmlFile;
import com.smoothstack.alinefinancial.maps.MerchantMap;
import com.smoothstack.alinefinancial.models.Merchant;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.FileWriter;

@Slf4j(topic = "XmlMerchantWriterTasklet")
public class XmlMerchantWriterTasklet implements Tasklet {

    private final MerchantMap merchantMap = MerchantMap.getInstance();

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            XStream merchantXStream = new XStream();
            merchantXStream.alias("Merchant", Merchant.class);
            merchantXStream.omitField(Merchant.class, "transactionsByAmt");
            FileWriter merchantFileWriter = new FileWriter("src/main/ProcessedOutFiles/XmlMerchants.xml");
            StringBuilder merchantStringBuilder = new StringBuilder();
            merchantStringBuilder.append(XmlFile.HEADER.toString());
            merchantStringBuilder.append("<Merchants>\n");

            merchantMap.getGeneratedMerchants().forEach((k, v) -> {
                merchantStringBuilder.append(merchantXStream.toXML(v));
            });
            merchantStringBuilder.append("\n</Merchants>");
            merchantFileWriter.append(merchantStringBuilder);
            merchantFileWriter.close();

        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }
}
