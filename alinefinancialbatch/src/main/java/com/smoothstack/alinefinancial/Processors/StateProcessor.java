package com.smoothstack.alinefinancial.Processors;

import com.smoothstack.alinefinancial.Caches.StateCache;
import com.smoothstack.alinefinancial.Models.State;
import com.smoothstack.alinefinancial.Models.Transaction;
import com.smoothstack.alinefinancial.Models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.listener.ItemListenerSupport;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j(topic = "StateProcessor")
public class StateProcessor extends ItemListenerSupport<Transaction, Object> implements ItemProcessor<Transaction, Object> {

    private static StateCache stateCache = new StateCache();

    @Override
    public Transaction process(Transaction item) throws Exception {
        try {
            stateCache.addSeenStatesAndZip(item);
        } catch (Exception e) {
            log.info(e.toString());
        }
        return null;
    }
}
