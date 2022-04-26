package com.smoothstack.alinefinancial.Processors;

import com.smoothstack.alinefinancial.Caches.CardCache;
import com.smoothstack.alinefinancial.Caches.UserCache;
import com.smoothstack.alinefinancial.Models.Transaction;
import org.springframework.batch.core.listener.ItemListenerSupport;
import org.springframework.batch.item.ItemProcessor;

public class UserProcessor extends ItemListenerSupport<Transaction, Object> implements ItemProcessor<Transaction, Object> {

    private static final UserCache userCache = UserCache.getInstance();
    private final CardCache cardCache = CardCache.getInstance();


    @Override
    public Transaction process(Transaction item) throws Exception {
        userCache.findUserOrGenerate(item.getUser());
        cardCache.findOrGenerateCard(item.getUser(), item.getCard());
        return item;
    }
}
