package com.smoothstack.alinefinancial.Generators;

import com.github.javafaker.Faker;
//import com.smoothstack.alinefinancial.Caches.CardCache;
import com.smoothstack.alinefinancial.Caches.CardCache;
import com.smoothstack.alinefinancial.Models.Card;
import com.smoothstack.alinefinancial.Models.User;
import com.vangogiel.luhnalgorithms.LuhnAlgorithms;
import org.springframework.stereotype.Component;

import java.util.HashMap;

public class CardGenerator {

    private static CardGenerator cardGeneratorInstance = null;

    private Long incrementId = 0L;

    public static CardGenerator getInstance(){
        if(cardGeneratorInstance == null) cardGeneratorInstance = new CardGenerator();
        return cardGeneratorInstance;
    }

    public synchronized void addGeneratedCard(Long userId, CardCache cc){
        cc.addGeneratedCard(userId, makeCard(userId));
    }

    private synchronized Card makeCard(Long userId){
        Card card = new Card();
        card.setId(incrementId);
        card.setNumber(Long.toString(LuhnAlgorithms.generateRandomLuhn(16)));
        card.setUserId(userId);
        incrementId = incrementId+1;
        System.out.println(card);
        return card;
    }

    private boolean validCard(String card){
        return LuhnAlgorithms.isValid(card);
    }
}