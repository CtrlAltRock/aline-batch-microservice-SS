package com.smoothstack.alinefinancial.Processors;

import com.smoothstack.alinefinancial.Maps.MerchantMap;
import com.smoothstack.alinefinancial.Models.Merchant;
import com.smoothstack.alinefinancial.Models.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j(topic="MerchantProcessor")
public class MerchantProcessor implements ItemProcessor<Transaction, Transaction> {

    private MerchantMap merchantCache = MerchantMap.getInstance();
    private Long transactionLine = 1L;

    @Override
    public Transaction process(Transaction item) throws Exception {
        try {
            Merchant merchant = merchantCache.findMerchantOrGenerate(item.getMerchant_name(), item.getMcc(), item.getAmount());
            transactionLine++;
        } catch (Exception e) {
            StringBuilder errorString = new StringBuilder();
            errorString.append(e);
            errorString.append(" on transaction line: ");
            errorString.append(transactionLine);

            log.info(errorString.toString());
        }
        return item;
    }
}
