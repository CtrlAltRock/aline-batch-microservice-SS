package com.smoothstack.alinefinancial.Processors;

import com.smoothstack.alinefinancial.Caches.CardCache;
import com.smoothstack.alinefinancial.Models.Card;
import com.smoothstack.alinefinancial.Models.Transaction;
import org.springframework.batch.core.listener.ItemListenerSupport;
import org.springframework.batch.item.ItemProcessor;

public class CardProcessor extends ItemListenerSupport<Transaction, Object> implements ItemProcessor<Transaction, Object> {

    private final CardCache cardCache = CardCache.getInstance();

    @Override
    public Transaction process(Transaction item) {
        cardCache.findOrGenerateCard(item.getUser(), item.getCard());
        return item;
    }
}
