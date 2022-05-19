package com.smoothstack.alinefinancial.tasklets.XmlWritingTasklets;

import com.smoothstack.alinefinancial.enums.XmlFile;
import com.smoothstack.alinefinancial.maps.AnalysisMap;
import com.smoothstack.alinefinancial.models.Transaction;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;

@Slf4j(topic = "XmlTransAfter8And100")
public class TransAfter8And100 implements Tasklet {

    // default filePath
    private String filePath = XmlFile.FILEPATH.toString();

    // default fileName
    private String fileName = XmlFile.TRANSACTIONAFTER8PMOVER100.toString();

    private final AnalysisMap analysisMap = AnalysisMap.getAnalysisMap();

    public TransAfter8And100(String filePath, String fileName) {
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
            FileWriter fileWriter = new FileWriter(Path.of(filePath, fileName).toString());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(XmlFile.HEADER.toString());

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
