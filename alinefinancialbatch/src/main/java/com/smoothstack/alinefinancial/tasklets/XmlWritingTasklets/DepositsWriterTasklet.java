package com.smoothstack.alinefinancial.tasklets.XmlWritingTasklets;

import com.smoothstack.alinefinancial.dto.UserDeposit;
import com.smoothstack.alinefinancial.enums.XmlFile;
import com.smoothstack.alinefinancial.maps.AnalysisMap;
import com.smoothstack.alinefinancial.models.Transaction;
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

//NRVNA - 83 - All card payments
@Slf4j(topic="XmlDepositWriterTasklet")
public class DepositsWriterTasklet implements Tasklet {

    // default filePath
    private String filePath = XmlFile.FILEPATH.toString();

    // default fileName
    private String fileName = "Cards.xml";

    private final AnalysisMap analysisMap = AnalysisMap.getAnalysisMap();

    public DepositsWriterTasklet(String filePath, String fileName) {
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
        try{
            XStream depositXStream = new XStream();
            depositXStream.alias("UserDeposit", UserDeposit.class);
            depositXStream.alias("Transaction", Transaction.class);
            depositXStream.omitField(User.class, "email");
            depositXStream.omitField(User.class, "cards");
            depositXStream.omitField(User.class, "insufficientBalanceTransactions");
            depositXStream.omitField(Transaction.class, "user");
            depositXStream.omitField(Transaction.class, "method");
            depositXStream.omitField(Transaction.class, "merchant_name");
            depositXStream.omitField(Transaction.class, "merchant_city");
            depositXStream.omitField(Transaction.class, "merchant_state");
            depositXStream.omitField(Transaction.class, "merchant_zip");
            depositXStream.omitField(Transaction.class, "mcc");
            depositXStream.omitField(Transaction.class, "errors");
            depositXStream.omitField(Transaction.class, "fraud");

            FileWriter userDepositsWriter = new FileWriter(Path.of(filePath, fileName).toString());
            StringBuilder depositsBuilder = new StringBuilder();
            depositsBuilder.append(XmlFile.HEADER.toString());
            depositsBuilder.append("<UserDeposits>\n");

            analysisMap.getUserDeposit().forEach( (k, v) -> {
                depositsBuilder.append(depositXStream.toXML(v));
            });
            depositsBuilder.append("\n</UserDeposits>");
            userDepositsWriter.append(depositsBuilder);
            userDepositsWriter.close();

        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }
}
