package com.smoothstack.alinefinancial.Tasklets;

import com.smoothstack.alinefinancial.Maps.UserMap;
import com.smoothstack.alinefinancial.Models.Transaction;
import com.smoothstack.alinefinancial.Models.User;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.FileWriter;

//NRVNA - 83 - All card payments
@Slf4j(topic="XmlDepositWriterTasklet")
public class XmlDepositsWriterTasklet implements Tasklet {

    private final UserMap userMap = UserMap.getInstance();
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try{
            XStream depositXStream = new XStream();
            depositXStream.alias("deposits", User.class);
            depositXStream.alias("transaction", Transaction.class);
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

            FileWriter userDepositsWriter = new FileWriter("src/main/ProcessedOutFiles/XmlUserDeposits.xml");
            StringBuilder depositsBuilder = new StringBuilder();
            depositsBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            depositsBuilder.append("<Users>\n");

            userMap.getGeneratedUsers().forEach( (k, v) -> {
                depositsBuilder.append(depositXStream.toXML(v));
            });
            depositsBuilder.append("\n</Users>");
            userDepositsWriter.append(depositsBuilder);
            userDepositsWriter.close();

        } catch (Exception e) {
            log.info(e.toString());
        }
        return null;
    }
}
