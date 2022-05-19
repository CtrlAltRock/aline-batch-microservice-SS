package com.smoothstack.alinefinancial.tasklets.XmlWritingTasklets;

import com.smoothstack.alinefinancial.enums.TransactionType;
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
import java.util.HashMap;

import static java.util.Objects.isNull;

@Slf4j(topic = "TypesOfTransactions" )
public class TypesOfTransactions implements Tasklet {

    // default filePath
    private String filePath = XmlFile.FILEPATH.toString();

    // default fileName
    private String fileName = XmlFile.TYPESOFTRANSACTIONS.toString();

    private AnalysisMap analysisMap = AnalysisMap.getAnalysisMap();

    public TypesOfTransactions(String filePath, String fileName) {
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
            HashMap<String, Long> typesOfTransactions = analysisMap.getTypesOfTransactions();
            com.smoothstack.alinefinancial.dto.TypesOfTransactions types = new com.smoothstack.alinefinancial.dto.TypesOfTransactions();
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
            stream.alias("TypesOfTransactions", com.smoothstack.alinefinancial.dto.TypesOfTransactions.class);
            FileWriter fileWriter = new FileWriter(Path.of(filePath, fileName).toString());
            fileWriter.append(XmlFile.HEADER.toString());
            fileWriter.append(stream.toXML(types));
            fileWriter.close();


        } catch (Exception e) {

        }
        return null;
    }
}
