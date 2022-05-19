package com.smoothstack.alinefinancial.tasklets.XmlWritingTasklets;

import com.smoothstack.alinefinancial.dto.RecurringTransaction;
import com.smoothstack.alinefinancial.dto.RecurringTransactionAndAmount;
import com.smoothstack.alinefinancial.enums.XmlFile;
import com.smoothstack.alinefinancial.maps.AnalysisMap;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

@Slf4j
public class RecurringTransactionWriterTasklet implements Tasklet {

    private AnalysisMap analysisMap = AnalysisMap.getAnalysisMap();

    // default filePath
    private String filePath = XmlFile.FILEPATH.toString();

    // default fileName
    private String fileName = XmlFile.RECURRINGTRANSACTION.toString();


    public RecurringTransactionWriterTasklet(String filePath, String fileName) {
        try{
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
            if(!isNull(analysisMap.getReportMap().get("recurring-transactions"))) {
                List<Map.Entry<RecurringTransaction, Long>> recurringTransactions = (List<Map.Entry<RecurringTransaction, Long>>)analysisMap.getReportMap().get("recurring-transactions");
                XStream stream = new XStream();
                stream.alias("RecurringTransactionAndAmount", RecurringTransactionAndAmount.class);
                stream.alias("RecurringTransaction", RecurringTransaction.class);
                FileWriter writer = new FileWriter(Path.of(filePath, fileName).toString());
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("<RecurringTransactions>\n");
                writer.append(XmlFile.HEADER.toString());
                recurringTransactions.forEach((n) -> {
                    stringBuilder.append(stream.toXML(new RecurringTransactionAndAmount(n.getKey(), n.getValue())));
                    stringBuilder.append("\n");
                });
                stringBuilder.append("\n</RecurringTransactions>");
                writer.append(stringBuilder);
                writer.close();
            }

        } catch (Exception e) {
            log.error(e.toString());
        }

        return null;
    }
}
