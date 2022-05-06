package com.smoothstack.alinefinancial.Generators;

import com.smoothstack.alinefinancial.Maps.CardMap;
import com.smoothstack.alinefinancial.Models.Card;
import com.vangogiel.luhnalgorithms.LuhnAlgorithms;

import java.util.HashSet;

public class CardGenerator {

    private static CardGenerator cardGeneratorInstance = null;

    private Long incrementId = 0L;

    public static CardGenerator getInstance(){
        if(cardGeneratorInstance == null)
            synchronized (CardGenerator.class) {
                if (cardGeneratorInstance == null) {
                    cardGeneratorInstance = new CardGenerator();
                }
            }
        return cardGeneratorInstance;
    }
    public void instantiateCard(Long userId, CardMap cardCache) {
        cardCache.getGeneratedCards().put(userId, new HashSet<>());
    }

    public void addGeneratedCard(Long userId, CardMap cc){
        cc.addGeneratedCard(userId, makeCard(userId));
    }

    private Card makeCard(Long userId){
        Card card = new Card();
        card.setId(incrementId);
        card.setNumber(Long.toString(LuhnAlgorithms.generateRandomLuhn(16)));
        card.setUserId(userId);
        incrementId += 1;
        return card;
    }

    private boolean validCard(String card){
        return LuhnAlgorithms.isValid(card);
    }

}