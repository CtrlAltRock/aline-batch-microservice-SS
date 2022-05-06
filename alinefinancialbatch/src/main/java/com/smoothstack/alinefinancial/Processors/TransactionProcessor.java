package com.smoothstack.alinefinancial.Processors;

import com.smoothstack.alinefinancial.Maps.TransactionCache;
import com.smoothstack.alinefinancial.Models.Transaction;
import org.springframework.batch.item.ItemProcessor;

public class TransactionProcessor implements ItemProcessor<Transaction, Transaction> {

    private static TransactionCache transactionCache = new TransactionCache();
    private Long index = 0L;

    @Override
    public Transaction process(Transaction item) throws Exception {
        transactionCache.addTransaction(index, item);
        index++;
        return item;
    }
}
