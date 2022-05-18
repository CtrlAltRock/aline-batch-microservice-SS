package com.smoothstack.alinefinancial.tasklets.XmlWritingTasklets;

import com.smoothstack.alinefinancial.enums.TransactionType;
import com.smoothstack.alinefinancial.maps.AnalysisMap;
import com.smoothstack.alinefinancial.xmlmodels.TypesOfTransactions;
import com.thoughtworks.xstream.XStream;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.FileWriter;
import java.util.HashMap;

import static java.util.Objects.isNull;

public class XmlTypesOfTransactions implements Tasklet {

    private AnalysisMap analysisMap = AnalysisMap.getAnalysisMap();

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try{
            HashMap<String, Long> typesOfTransactions = analysisMap.getTypesOfTransactions();
            TypesOfTransactions types = new TypesOfTransactions();
            if(!Integer.valueOf(typesOfTransactions.size()).equals(0)) {
                if(!isNull(typesOfTransactions.get(TransactionType.SWIPE.toString()))) {
                    types.setSwipe(typesOfTransactions.get(TransactionType.SWIPE.toString()));
                }

                if(!isNull(typesOfTransactions.get(TransactionType.ONLINE.toString()))) {
                    types.setOnline(typesOfTransactions.get(TransactionType.ONLINE.toString()));
                }

                if(!isNull(typesOfTransactions.get(TransactionType.CHIP.toString()))) {
                    types.setChip(typesOfTransactions.get(TransactionType.CHIP.toString()));
                }
            }

            XStream stream = new XStream();
            stream.alias("TypesOfTransactions", TypesOfTransactions.class);
            FileWriter fileWriter = new FileWriter("src/main/ProcessedOutFiles/XmlTypesOfTransactions.xml");
            fileWriter.append(stream.toXML(types));
            fileWriter.close();


        } catch (Exception e) {

        }
        return null;
    }
}
