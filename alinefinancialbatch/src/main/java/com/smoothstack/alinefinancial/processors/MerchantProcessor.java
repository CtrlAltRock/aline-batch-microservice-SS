package com.smoothstack.alinefinancial.processors;

import com.smoothstack.alinefinancial.maps.AnalysisMap;
import com.smoothstack.alinefinancial.maps.MerchantMap;
import com.smoothstack.alinefinancial.models.Merchant;
import com.smoothstack.alinefinancial.models.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j(topic="MerchantProcessor")
public class MerchantProcessor implements ItemProcessor<Transaction, Transaction> {

    private MerchantMap merchantMap = MerchantMap.getInstance();
    private AnalysisMap analysisMap = AnalysisMap.getInstance();
    private Long transactionLine = 1L;

    @Override
    public Transaction process(Transaction item) throws Exception {
        try {
            Merchant merchant = merchantMap.findMerchantOrGenerate( item.getMerchant_name(), item.getMcc());
            // NRVNA - 86 Top five recurring transactions by merchant
            analysisMap.addMerchantTransaction(merchant, item.getAmount());
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
