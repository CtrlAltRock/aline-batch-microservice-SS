package com.smoothstack.alinefinancial.Processors;

import com.smoothstack.alinefinancial.Caches.CardCache;
import com.smoothstack.alinefinancial.Caches.UserCache;
import com.smoothstack.alinefinancial.Models.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.listener.ItemListenerSupport;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j(topic = "UserProcessor")
public class UserProcessor extends ItemListenerSupport<Transaction, Object> implements ItemProcessor<Transaction, Object> {

    private static UserCache userCache = UserCache.getInstance();

    private CardCache cardCache = CardCache.getInstance();


    @Override
    public Transaction process(Transaction item) throws Exception {
        try {
            userCache.findUserOrGenerate(item.getUser());
            cardCache.findOrGenerateCard(item.getUser(), item.getCard());
        } catch (Exception e) {
            log.info(e.toString());
        }
        return null;
    }
}
