package com.smoothstack.alinefinancial.Maps;

import com.smoothstack.alinefinancial.Models.Transaction;

import java.util.HashMap;

public class TransactionCache {
    HashMap<Long, Transaction> transactionMap = new HashMap<Long, Transaction>();

    private static TransactionCache transactionCacheInstance = null;

    public static TransactionCache getInstance() {
        if(transactionCacheInstance == null) {
            transactionCacheInstance = new TransactionCache();
        }
        return transactionCacheInstance;
    }

    public HashMap<Long, Transaction> getMap() {
        return getInstance().transactionMap;
    }

    public Transaction getTransaction(Long id) {
        return getInstance().transactionMap.get(id);
    }

    public synchronized void addTransaction(Long id, Transaction transaction) {
        if(getTransaction(id) == null) {
            getInstance().transactionMap.put(id, transaction);
        }
    }
    
}
