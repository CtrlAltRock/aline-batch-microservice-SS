package com.smoothstack.alinefinancial.Processors;

import com.smoothstack.alinefinancial.Caches.CardCache;
import com.smoothstack.alinefinancial.Caches.UserCache;
import com.smoothstack.alinefinancial.Generators.CardGenerator;
import com.smoothstack.alinefinancial.Models.Card;
import com.smoothstack.alinefinancial.Models.Transaction;
import com.smoothstack.alinefinancial.Models.TransactionV2;
import com.smoothstack.alinefinancial.Models.User;
import org.springframework.batch.core.listener.ItemListenerSupport;
import org.springframework.batch.item.ItemProcessor;

public class UserProcessor extends ItemListenerSupport<Transaction, User> implements ItemProcessor<Transaction, User> {

    private static final UserCache userCache = UserCache.getInstance();

    private final CardCache cardCache = CardCache.getInstance();


    @Override
    public User process(Transaction item) throws Exception {
        User user = userCache.findUserOrGenerate(item.getUser());
        //Card Gen
        cardCache.findOrGenerateCard(item.getUser(), item.getCard());

        return user;
    }
}
