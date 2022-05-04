package com.smoothstack.alinefinancial.Maps;


import com.smoothstack.alinefinancial.Generators.CardGenerator;
import com.smoothstack.alinefinancial.Models.Card;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.HashSet;

@Slf4j(topic="CardCache")
public class CardMap {
    private final HashMap<Long, HashSet<Card>> generatedCards = new HashMap<>();

    private static CardMap cardCacheInstance = null;

    public static CardMap getInstance(){
        if(cardCacheInstance == null) {
            cardCacheInstance = new CardMap();
        }
        return cardCacheInstance;
    }

    public void addGeneratedCard(Long userId, Card card){
        HashSet<Card> cards = generatedCards.get(userId);
        if(cards == null) cards = new HashSet<>();
        cards.add(card);
        generatedCards.put(userId, cards);
    }

    public synchronized HashSet<Card> getGeneratedUserCards(Long userId){
        return generatedCards.get(userId);
    }

    public synchronized HashMap<Long, HashSet<Card>> getGeneratedCards(){
        return generatedCards;
    }

    public synchronized void findOrGenerateCard(Long userId, Long cardIndex) {
        CardGenerator cardGenerator = CardGenerator.getInstance();
        if(getGeneratedUserCards(userId) == null) {
            synchronized (CardGenerator.class) {
                if(getGeneratedUserCards(userId) == null) cardGenerator.addGeneratedCard(userId, this);
            }
        }
        if(getGeneratedUserCards(userId).size() < cardIndex + 1) {
            synchronized (CardGenerator.class)
            {
                if(getGeneratedUserCards(userId).size() < cardIndex + 1){
                    for(int i = getGeneratedUserCards(userId).size(); i<=cardIndex + 1; i++) {
                        cardGenerator.addGeneratedCard(userId, this);
                    }
                }
            }
        }
    }
}



