package com.smoothstack.alinefinancial.processors;

import com.smoothstack.alinefinancial.enums.Errors;
import com.smoothstack.alinefinancial.maps.AnalysisMap;
import com.smoothstack.alinefinancial.maps.CardMap;
import com.smoothstack.alinefinancial.maps.UserMap;
import com.smoothstack.alinefinancial.models.Transaction;
import com.smoothstack.alinefinancial.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.util.Arrays;
import java.util.List;

@Slf4j(topic = "UserProcessor")
public class UserProcessor implements ItemProcessor<Transaction, Transaction> {

    private static UserMap userMap = UserMap.getInstance();
    private CardMap cardMap = CardMap.getInstance();
    private AnalysisMap analysisMap = AnalysisMap.getAnalysisMap();

    private Long transactionLine = 1L;

    @Override
    public Transaction process(Transaction item) throws Exception {
        try {
            User user = userMap.findUserOrGenerate(item.getUser());
            cardMap.findOrGenerateCard(item.getUser(), item.getCard());

            // NRVNA- 84 & 85 Insufficient Balance incrementer
            if(!item.getErrors().isBlank()) {
                List<String> errors = Arrays.asList(item.getErrors().split(","));
                if(errors.contains(Errors.INSUFFICIENT.toString())) {
                    analysisMap.addToInsufficientBalance(user);
                }
            }

            // NRVNA - 83 - All card payments for a user, assuming a negative amount is a payment
            if(Double.parseDouble(item.getAmount().replace("$", "")) < 0) {
                analysisMap.addToUserDeposits(user, item);
            }

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
