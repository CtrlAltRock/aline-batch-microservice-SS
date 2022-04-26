package com.smoothstack.alinefinancial.Processors;

import com.smoothstack.alinefinancial.Caches.StateCache;
import com.smoothstack.alinefinancial.Models.State;
import com.smoothstack.alinefinancial.Models.Transaction;
import com.smoothstack.alinefinancial.Models.User;
import org.springframework.batch.core.listener.ItemListenerSupport;
import org.springframework.batch.item.ItemProcessor;

public class StateProcessor extends ItemListenerSupport<Transaction, State> implements ItemProcessor<Transaction, State> {

    private static StateCache stateCache = new StateCache();

    @Override
    public State process(Transaction item) throws Exception {
        State state = stateCache.addSeenStatesAndZip(item.getMerchant_state(), item.getMerchant_zip());
        //System.out.println(state);
        return state;
    }
}
