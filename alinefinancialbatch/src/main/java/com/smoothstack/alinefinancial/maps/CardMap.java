package com.smoothstack.alinefinancial.maps;


import com.smoothstack.alinefinancial.generators.CardGenerator;
import com.smoothstack.alinefinancial.models.Card;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.HashSet;

@Slf4j(topic="CardMap")
public class CardMap {
    private final HashMap<Long, HashSet<Card>> generatedCards = new HashMap<>();

    private static CardMap cardMapInstance = null;

    public static CardMap getInstance(){
        if(cardMapInstance == null) {
            cardMapInstance = new CardMap();
        }
        return cardMapInstance;
    }

    public synchronized void addGeneratedCard(Long userId, Card card){
        try {
            HashSet<Card> cards = generatedCards.get(userId);
            if (cards == null) cards = new HashSet<>();
            cards.add(card);
            generatedCards.put(userId, cards);
        } catch (Exception e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Method: addGenerateCard\tException: ");
            errorMessage.append(e);
            log.error(errorMessage.toString());
        }
    }

    public synchronized HashSet<Card> getGeneratedUserCards(Long userId){
        return generatedCards.get(userId);
    }

    public synchronized HashMap<Long, HashSet<Card>> getGeneratedCards(){
        return generatedCards;
    }

    public synchronized void findOrGenerateCard(Long userId, Long cardIndex) {
        try {
            CardGenerator cardGenerator = CardGenerator.getInstance();
            if (getGeneratedUserCards(userId) == null) {
                synchronized (CardGenerator.class) {
                    if (getGeneratedUserCards(userId) == null) cardGenerator.addGeneratedCard(userId, this);
                }
            }
            if (getGeneratedUserCards(userId).size() < cardIndex + 1) {
                synchronized (CardGenerator.class) {
                    if (getGeneratedUserCards(userId).size() < cardIndex + 1) {
                        for (int i = getGeneratedUserCards(userId).size(); i <= cardIndex + 1; i++) {
                            cardGenerator.addGeneratedCard(userId, this);
                        }
                    }
                }
            }
        } catch (Exception e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Method: findOrGenerateCard\tException: ");
            errorMessage.append(e);
            log.error(errorMessage.toString());
        }
    }
}



