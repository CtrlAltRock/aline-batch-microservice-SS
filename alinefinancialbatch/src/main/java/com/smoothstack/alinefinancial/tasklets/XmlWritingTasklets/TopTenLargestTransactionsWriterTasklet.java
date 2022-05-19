package com.smoothstack.alinefinancial.tasklets.XmlWritingTasklets;

import com.smoothstack.alinefinancial.dto.TopTenLargestTransactions;
import com.smoothstack.alinefinancial.enums.StatisticStrings;
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

import static java.util.Objects.isNull;

@Slf4j(topic = "XmlTopTenLargestTransactionsWriterTasklet")
public class TopTenLargestTransactionsWriterTasklet implements Tasklet {

    // default filePath
    private String filePath = XmlFile.FILEPATH.toString();

    // default fileName
    private String fileName = XmlFile.TOPTENLARGESTTRANSACTIONS.toString();

    private AnalysisMap analysisMap = AnalysisMap.getAnalysisMap();

    public TopTenLargestTransactionsWriterTasklet(String filePath, String fileName) {
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
            if (!isNull(analysisMap.getReportMap().get(StatisticStrings.TOPTENLARGESTTRANSACTIONS.toString()))) {
                TopTenLargestTransactions transactions = new TopTenLargestTransactions(analysisMap.getLargestTransactions());
                XStream stream = new XStream();
                stream.alias("TopTenLargestTransactions", TopTenLargestTransactions.class);
                stream.alias("Transaction", Transaction.class);
                FileWriter file = new FileWriter(Path.of(filePath, fileName).toString());
                StringBuilder fileBuilder = new StringBuilder();
                fileBuilder.append(XmlFile.HEADER);
                fileBuilder.append(stream.toXML(transactions));
                file.append(fileBuilder);
                file.close();
            }
        } catch (Exception e) {
            log.error(e.toString());
        }

        return null;
    }
}
