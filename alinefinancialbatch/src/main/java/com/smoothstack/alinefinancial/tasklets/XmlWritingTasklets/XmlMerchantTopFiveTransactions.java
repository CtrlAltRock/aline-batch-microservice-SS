package com.smoothstack.alinefinancial.tasklets.XmlWritingTasklets;

import com.smoothstack.alinefinancial.xmlmodels.MerchantTransaction;
import com.smoothstack.alinefinancial.enums.XmlFile;
import com.smoothstack.alinefinancial.maps.AnalysisMap;
import com.smoothstack.alinefinancial.models.Merchant;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.FileWriter;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j(topic = "XmlMerchantTopFiveTransactions")
public class XmlMerchantTopFiveTransactions implements Tasklet {

    private final AnalysisMap analysisMap = AnalysisMap.getAnalysisMap();
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try{
            XStream stream = new XStream();
            stream.alias("MerchantTransaction", MerchantTransaction.class);
            stream.omitField(Merchant.class, "transactionsByAmt");
            FileWriter fileWriter = new FileWriter("src/main/ProcessedOutFiles/XmlMerchantsTopFiveRecurringTransactions.xml");
            StringBuilder fileBuilder = new StringBuilder();
            fileBuilder.append(XmlFile.HEADER.toString());
            fileBuilder.append("<MerchantTransactions>\n");

            // NRVNA-86 Identify top 5 recurring transactions grouped by merchant
            analysisMap.getTransactionsByAmtByMerchant().forEach((k, v) -> {
                String transactions = v.entrySet()
                            .stream()
                            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                            .filter(n -> n.getValue() > 1)
                            .limit(5)
                            .collect(Collectors.toList()).toString();

                fileBuilder.append(stream.toXML(new MerchantTransaction(k, transactions)));
            });

            fileBuilder.append("\n</MerchantTransactions>");
            fileWriter.append(fileBuilder);
            fileWriter.close();
        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }
}
