package com.smoothstack.alinefinancial.Processors;

import com.smoothstack.alinefinancial.Caches.*;
import com.smoothstack.alinefinancial.Generators.CardGenerator;
import com.smoothstack.alinefinancial.Models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.listener.ItemListenerSupport;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@Slf4j(topic = "TransactionProcessor")
public class TransactionProcessor extends ItemListenerSupport<Transaction, Transaction> implements ItemProcessor<Transaction, TransactionV2> {

    private static UserCache userCache = new UserCache();

    private static MerchantCache merchantCache = new MerchantCache();

    private static MccCache mccCache = new MccCache();

    //private static CardCache cardCache = new CardCache();

    private final CardGenerator cardGenerator = CardGenerator.getInstance();

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {

    }

    @Override
    public TransactionV2 process(Transaction item) throws Exception {
        TransactionV2 transformed = new TransactionV2();
        Merchant merchant = merchantCache.findMerchantOrGenerate(item.getMerchant_name());
        MCC mcc = mccCache.findMccOrGenerate(item.getMcc());
        User user = userCache.findUserOrGenerate(item.getUser());
        //Card card = cardCache.findCardOrGenerate(user);

        /*if(item.getCard()+1>=user.getCards().size()) {
            for(int i = user.getCards().size(); i<=item.getCard()+1; i++) {
                if (user.getCards().isEmpty() || item.getCard().intValue()+1 > user.getCards().size()){
                    synchronized (CardGenerator.class) {
                        if (user.getCards().isEmpty() || item.getCard().intValue()+1 > user.getCards().size()){
                            Card card = cardGenerator.makeCard(user);
                            user.setCard(card);
                        }
                    }
                }
            }
        }*/


        transformed.setUser(user);
        transformed.setYear(item.getYear());
        transformed.setMonth(item.getMonth());
        transformed.setDay(item.getDay());

        transformed.setAmount(item.getAmount());
        transformed.setTime(item.getTime());
        transformed.setMethod(item.getMethod());
        transformed.setFraud(item.getFraud());
        transformed.setErrors(item.getErrors());


        merchant.setId(item.getMerchant_name());
        merchant.setCity(item.getMerchant_city());
        merchant.setState(item.getMerchant_state());
        merchant.setZip(item.getMerchant_zip());
        merchant.setMcc(mcc);
        transformed.setMerchant(merchant);
        return transformed;
    }
}
