package com.smoothstack.alinefinancial.processors;

import com.smoothstack.alinefinancial.maps.TransactionMap;
import com.smoothstack.alinefinancial.models.Transaction;
import org.springframework.batch.item.ItemProcessor;

public class TransactionProcessor implements ItemProcessor<Transaction, Transaction> {

    private static TransactionMap transactionMap = new TransactionMap();
    private Long index = 0L;

    @Override
    public Transaction process(Transaction item) throws Exception {
        transactionMap.addTransaction(index, item);
        index++;
        return item;
    }
}
