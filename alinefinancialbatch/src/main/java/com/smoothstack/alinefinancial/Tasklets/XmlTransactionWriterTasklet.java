package com.smoothstack.alinefinancial.Tasklets;

import com.smoothstack.alinefinancial.Maps.TransactionMap;
import com.smoothstack.alinefinancial.Models.Transaction;
import com.thoughtworks.xstream.XStream;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.FileWriter;

public class XmlTransactionWriterTasklet implements Tasklet {

    private final TransactionMap transactionMap = TransactionMap.getInstance();

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        XStream transactionXStream = new XStream();
        transactionXStream.alias("Transaction", Transaction.class);
        FileWriter transactionsFileWriter = new FileWriter("src/main/ProcessedOutFiles/XmlTransactions.xml");
        StringBuilder transactionsStringBuilder = new StringBuilder();
        transactionsStringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        transactionsStringBuilder.append("<Transaction>\n");
        transactionMap.getMap().forEach((k, v) -> {
            System.out.println(v.toString());
            if (v != null) transactionsStringBuilder.append(transactionXStream.toXML(v));
        });
        transactionsStringBuilder.append("\n</Transaction>");
        transactionsFileWriter.append(transactionsStringBuilder);
        transactionsFileWriter.close();
        return null;
    }
}
