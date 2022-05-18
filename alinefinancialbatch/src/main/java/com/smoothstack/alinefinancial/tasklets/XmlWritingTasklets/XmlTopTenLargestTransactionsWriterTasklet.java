package com.smoothstack.alinefinancial.tasklets.XmlWritingTasklets;

import com.smoothstack.alinefinancial.xmlmodels.TopTenLargestTransactions;
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

import java.io.FileWriter;

import static java.util.Objects.isNull;

@Slf4j(topic = "XmlTopTenLargestTransactionsWriterTasklet")
public class XmlTopTenLargestTransactionsWriterTasklet implements Tasklet {

    private AnalysisMap analysisMap = AnalysisMap.getAnalysisMap();

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            if (!isNull(analysisMap.getReportMap().get(StatisticStrings.TOPTENLARGESTTRANSACTIONS.toString()))) {
                TopTenLargestTransactions transactions = new TopTenLargestTransactions(analysisMap.getLargestTransactions());
                XStream stream = new XStream();
                stream.alias("TopTenLargestTransactions", TopTenLargestTransactions.class);
                stream.alias("Transaction", Transaction.class);
                FileWriter file = new FileWriter("src/main/ProcessedOutFiles/XmlTopTenLargestTransactions.xml");
                StringBuilder fileBuilder = new StringBuilder();
                fileBuilder.append(XmlFile.HEADER.toString());
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
