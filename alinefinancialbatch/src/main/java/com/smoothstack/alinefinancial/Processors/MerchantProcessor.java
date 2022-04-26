package com.smoothstack.alinefinancial.Processors;

import com.smoothstack.alinefinancial.Caches.MerchantCache;
import com.smoothstack.alinefinancial.Models.Merchant;
import com.smoothstack.alinefinancial.Models.Transaction;
import org.springframework.batch.core.listener.ItemListenerSupport;
import org.springframework.batch.item.ItemProcessor;

public class MerchantProcessor extends ItemListenerSupport<Transaction, Object> implements ItemProcessor<Transaction, Object> {

    private final MerchantCache merchantCache = MerchantCache.getInstance();

    @Override
    public Transaction process(Transaction item) throws Exception {
        Merchant merchant = merchantCache.findMerchantOrGenerate(item.getMerchant_name(), item.getMcc());
        //MCC mcc = mccCache.findMccOrGenerate(item.getMcc());
        return item;
    }
}
