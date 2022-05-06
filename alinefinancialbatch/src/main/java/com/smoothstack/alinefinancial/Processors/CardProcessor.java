package com.smoothstack.alinefinancial.Processors;

import com.smoothstack.alinefinancial.Maps.CardMap;
import com.smoothstack.alinefinancial.Models.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j(topic = "CardProcessor")
public class CardProcessor implements ItemProcessor<Transaction, Transaction> {

    private final CardMap cardCache = CardMap.getInstance();
    private Long transactionLine = 1L;

    @Override
    public Transaction process(Transaction item) {
        try {
            cardCache.findOrGenerateCard(item.getUser(), item.getCard());
            transactionLine++;
        } catch (Exception e) {
            StringBuilder errorString = new StringBuilder();
            errorString.append(e);
            errorString.append(" on ");
            errorString.append(transactionLine);

            log.info(errorString.toString());
        }
        return item;
    }
}
