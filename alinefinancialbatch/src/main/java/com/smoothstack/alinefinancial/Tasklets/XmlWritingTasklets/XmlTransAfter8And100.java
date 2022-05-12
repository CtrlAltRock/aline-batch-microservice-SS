package com.smoothstack.alinefinancial.Tasklets.XmlWritingTasklets;

import com.smoothstack.alinefinancial.Maps.AnalysisMap;
import com.smoothstack.alinefinancial.Models.Transaction;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.FileWriter;

@Slf4j(topic = "XmlTransAfter8And100")
public class XmlTransAfter8And100 implements Tasklet {

    private final AnalysisMap analysisMap = AnalysisMap.getInstance();

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            XStream stream = new XStream();
            stream.alias("Transaction", Transaction.class);
            stream.omitField(Transaction.class, "user");
            stream.omitField(Transaction.class, "method");
            stream.omitField(Transaction.class, "merchant_name");
            stream.omitField(Transaction.class, "merchant_city");
            stream.omitField(Transaction.class, "merchant_state");
            stream.omitField(Transaction.class, "merchant_zip");
            stream.omitField(Transaction.class, "mcc");
            stream.omitField(Transaction.class, "errors");
            stream.omitField(Transaction.class, "fraud");
            FileWriter fileWriter = new FileWriter("src/main/ProcessedOutFiles/XmlTransactionsOver100And8PM.xml");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

            analysisMap.getTransAfter8Above100().forEach((k, v) -> {
                stringBuilder.append(k);
                stringBuilder.append(stream.toXML(v));
            });

            fileWriter.append(stringBuilder);
            fileWriter.close();

        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }
}
