package com.smoothstack.alinefinancial.processors;

import com.smoothstack.alinefinancial.maps.StateMap;
import com.smoothstack.alinefinancial.models.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j(topic = "StateProcessor")
public class StateProcessor implements ItemProcessor<Transaction, Transaction> {

    private static StateMap stateMap = new StateMap();
    private Long transactionLine = 1L;

    @Override
    public Transaction process(Transaction item) throws Exception {
        try {
            stateMap.addSeenStatesAndZip(item);
            transactionLine++;
        } catch (Exception e) {
            StringBuilder errorString = new StringBuilder();
            errorString.append(e);
            errorString.append(" on transaction line: ");
            errorString.append(transactionLine);

            log.error(item.toString());
            log.error(errorString.toString());
        }
        return item;
    }
}
