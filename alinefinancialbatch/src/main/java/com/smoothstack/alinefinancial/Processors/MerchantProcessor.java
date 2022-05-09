package com.smoothstack.alinefinancial.Processors;

import com.smoothstack.alinefinancial.Maps.MerchantMap;
import com.smoothstack.alinefinancial.Models.Merchant;
import com.smoothstack.alinefinancial.Models.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j(topic="MerchantProcessor")
public class MerchantProcessor implements ItemProcessor<Transaction, Transaction> {

    private MerchantMap merchantMap = MerchantMap.getInstance();
    private Long transactionLine = 1L;

    @Override
    public Transaction process(Transaction item) throws Exception {
        try {
            Merchant merchant = merchantMap.findMerchantOrGenerate(transactionLine, item.getMerchant_name(), item.getMcc(), item.getAmount());
            merchantMap.findGeneratedMerchantAndAddTransactionAmt(item.getMerchant_name(), item.getAmount());
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
