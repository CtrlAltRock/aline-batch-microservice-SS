package com.smoothstack.alinefinancial.generators;

import com.smoothstack.alinefinancial.maps.CardMap;
import com.smoothstack.alinefinancial.models.Card;
import com.vangogiel.luhnalgorithms.LuhnAlgorithms;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;

@Slf4j(topic = "CardGenerator")
public class CardGenerator {

    private static CardGenerator cardGeneratorInstance = null;

    private Long incrementId = 0L;

    public static CardGenerator getInstance(){
        try {
            if (cardGeneratorInstance == null)
                synchronized (CardGenerator.class) {
                    if (cardGeneratorInstance == null) {
                        cardGeneratorInstance = new CardGenerator();
                    }
                }
        } catch (Exception e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Method: getInstance\tException: ");
            errorMessage.append(e);
            log.error(errorMessage.toString());
        }
        return cardGeneratorInstance;
    }

    public void instantiateCard(Long userId, CardMap cardCache) {
        try {
            cardCache.getGeneratedCards().put(userId, new HashSet<>());
        } catch (Exception e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Method: instantiateCard\tException: ");
            errorMessage.append(e);
            log.error(errorMessage.toString());
        }
    }

    public synchronized void addGeneratedCard(Long userId, CardMap cc){
        try {
            cc.addGeneratedCard(userId, makeCard(userId));
        } catch (Exception e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Method: addGeneratedCard\tException: ");
            errorMessage.append(e);
            log.error(errorMessage.toString());
        }
    }

    private synchronized Card makeCard(Long userId){
        Card card = new Card();
        try {
            card.setId(incrementId);
            card.setNumber(Long.toString(LuhnAlgorithms.generateRandomLuhn(16)));
            card.setUserId(userId);
            incrementId += 1;
        } catch (Exception e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Method: makeCard\tException: ");
            errorMessage.append(e);
            log.error(errorMessage.toString());
        }
        return card;
    }

    private boolean validCard(String card){
        return LuhnAlgorithms.isValid(card);
    }

}