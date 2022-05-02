package com.smoothstack.alinefinancial.Caches;


import com.smoothstack.alinefinancial.Generators.CardGenerator;
import com.smoothstack.alinefinancial.Models.Card;
import com.smoothstack.alinefinancial.Processors.CardProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j(topic="CardCache")
public class CardCache {
    private final HashMap<Long, HashSet<Card>> syncGeneratedCards = new HashMap<>();
    //private final Map<Long, HashSet<Card>> syncGeneratedCards = Collections.synchronizedMap(new HashMap<>());

    private static CardCache cardCacheInstance = null;

    public static CardCache getInstance(){
        if(cardCacheInstance == null) {
            synchronized (CardCache.class) {
                if(cardCacheInstance == null) {
                    cardCacheInstance = new CardCache();
                }
            }
        }
        return cardCacheInstance;
    }

    public void addGeneratedCard(Long userId, Card card){
        HashSet<Card> cards = syncGeneratedCards.get(userId);
        if(cards == null) cards = new HashSet<>();
        cards.add(card);
        syncGeneratedCards.put(userId, cards);
    }

    public HashSet<Card> getGeneratedUserCards(Long userId){
        return syncGeneratedCards.get(userId);
    }

    public HashMap<Long, HashSet<Card>> getGeneratedCards(){
        return syncGeneratedCards;
    }

    public void findOrGenerateCard(Long userId, Long cardIndex) {
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



