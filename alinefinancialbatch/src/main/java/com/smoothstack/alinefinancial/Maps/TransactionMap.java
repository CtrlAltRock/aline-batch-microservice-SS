package com.smoothstack.alinefinancial.Maps;

import com.smoothstack.alinefinancial.Models.Transaction;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j(topic = "TransactionMap")
public class TransactionMap {
    HashMap<Long, Transaction> transactionMap = new HashMap<Long, Transaction>();

    private static TransactionMap transactionMapInstance = null;

    public static TransactionMap getInstance() {
        if(transactionMapInstance == null) {
            transactionMapInstance = new TransactionMap();
        }
        return transactionMapInstance;
    }

    public HashMap<Long, Transaction> getMap() {
        return getInstance().transactionMap;
    }

    public Transaction getTransaction(Long id) {
        return getInstance().transactionMap.get(id);
    }

    public synchronized void addTransaction(Long id, Transaction transaction) {
        try {
            if (getTransaction(id) == null) {
                getInstance().transactionMap.put(id, transaction);
            }
        } catch (Exception e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Method: addTransaction\tException: ");
            errorMessage.append(e);
            log.error(errorMessage.toString());
        }
    }
    
}
