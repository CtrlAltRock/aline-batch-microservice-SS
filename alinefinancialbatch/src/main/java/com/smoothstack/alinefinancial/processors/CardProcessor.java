package com.smoothstack.alinefinancial.processors;

import com.smoothstack.alinefinancial.maps.AnalysisMap;
import com.smoothstack.alinefinancial.maps.CardMap;
import com.smoothstack.alinefinancial.models.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j(topic = "CardProcessor")
public class CardProcessor implements ItemProcessor<Transaction, Transaction> {

    private final AnalysisMap analysisMap = AnalysisMap.getAnalysisMap();
    private final CardMap cardMap = CardMap.getInstance();
    private Long transactionLine = 1L;

    @Override
    public Transaction process(Transaction item) {
        try {
            cardMap.findOrGenerateCard(item.getUser(), item.getCard());

            transactionLine++;
        } catch (Exception e) {
            StringBuilder errorString = new StringBuilder();
            errorString.append(e);
            errorString.append(" on transaction line: ");
            errorString.append(transactionLine);

            log.error(item.toString());
            log.error(errorString.toString());
        }
        return item;
    }
}
