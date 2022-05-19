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

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;

@Slf4j(topic = "XmlMerchantWriterTasklet")
public class MerchantWriterTasklet implements Tasklet {

    // default filePath
    private String filePath = XmlFile.FILEPATH.toString();

    // default fileName
    private String fileName = "Cards.xml";

    private final MerchantMap merchantMap = MerchantMap.getInstance();

    public MerchantWriterTasklet(String filePath, String fileName) {
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
            XStream merchantXStream = new XStream();
            merchantXStream.alias("Merchant", Merchant.class);
            merchantXStream.omitField(Merchant.class, "transactionsByAmt");
            FileWriter merchantFileWriter = new FileWriter(Path.of(filePath, fileName).toString());
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
