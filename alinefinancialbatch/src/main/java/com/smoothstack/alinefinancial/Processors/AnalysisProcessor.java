package com.smoothstack.alinefinancial.Processors;

import com.smoothstack.alinefinancial.Maps.AnalysisMap;
import com.smoothstack.alinefinancial.Models.Transaction;
import org.springframework.batch.item.ItemProcessor;

public class AnalysisProcessor implements ItemProcessor<Transaction, Transaction> {

    private final AnalysisMap analysisMap = AnalysisMap.getInstance();

    @Override
    public Transaction process(Transaction item) throws Exception {
        return item;
    }
}
