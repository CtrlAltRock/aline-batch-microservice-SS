package com.smoothstack.alinefinancial.Processors;

import com.smoothstack.alinefinancial.Maps.StateMap;
import com.smoothstack.alinefinancial.Models.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j(topic = "StateProcessor")
public class StateProcessor implements ItemProcessor<Transaction, Transaction> {

    private static StateMap stateCache = new StateMap();
    private Long transactionLine = 1L;

    @Override
    public Transaction process(Transaction item) throws Exception {
        try {
            stateCache.addSeenStatesAndZip(item);
            transactionLine++;
        } catch (Exception e) {
            StringBuilder errorString = new StringBuilder();
            errorString.append(e);
            errorString.append(" on ");
            errorString.append(transactionLine);

            log.info(errorString.toString());
        }
        return item;
    }
}
