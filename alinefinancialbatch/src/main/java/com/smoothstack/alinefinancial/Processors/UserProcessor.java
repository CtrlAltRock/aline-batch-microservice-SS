package com.smoothstack.alinefinancial.Processors;

import com.smoothstack.alinefinancial.Maps.AnalysisMap;
import com.smoothstack.alinefinancial.Maps.CardMap;
import com.smoothstack.alinefinancial.Maps.UserMap;
import com.smoothstack.alinefinancial.Models.Transaction;
import com.smoothstack.alinefinancial.Models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.util.Arrays;
import java.util.List;

@Slf4j(topic = "UserProcessor")
public class UserProcessor implements ItemProcessor<Transaction, Transaction> {

    private static UserMap userMap = UserMap.getInstance();
    private CardMap cardMap = CardMap.getInstance();
    private AnalysisMap analysisMap = AnalysisMap.getInstance();

    private Long transactionLine = 1L;
    private Long numberOfUsersWithInsufficientBalance = 0L;
    private Long numberOfUsersWithInsufficientBalanceMoreThanOnce = 0L;

    @Override
    public Transaction process(Transaction item) throws Exception {
        try {
            User user = userMap.findUserOrGenerate(item.getUser());
            cardMap.findOrGenerateCard(item.getUser(), item.getCard());
            if(!item.getErrors().equals("")) {
                List<String> errors = Arrays.asList(item.getErrors().split(","));
                if(errors.contains("Insufficient Balance")) {
                    // should never be null since user above is either found or generated
                    userMap.getGeneratedUsers().get(user.getId()).setInsufficientBalanceTransactions(user.getInsufficientBalanceTransactions()+1);
                }
            }

            // NRVNA - 83 - All card payments for a user, assuming a negative amount is a payment
            if(Double.parseDouble(item.getAmount().replace("$", "")) < 0) {
                userMap.getGeneratedUsers().get(user.getId()).addDeposit(item);
            }

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
