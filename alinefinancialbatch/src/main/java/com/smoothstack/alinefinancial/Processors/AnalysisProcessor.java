package com.smoothstack.alinefinancial.Processors;

import com.smoothstack.alinefinancial.Caches.AnalysisMap;
import com.smoothstack.alinefinancial.Caches.MerchantCache;
import com.smoothstack.alinefinancial.Models.Transaction;
import org.springframework.batch.core.listener.ItemListenerSupport;
import org.springframework.batch.item.ItemProcessor;

public class AnalysisProcessor extends ItemListenerSupport<Transaction, Transaction>  implements ItemProcessor<Transaction, Transaction> {

    private final AnalysisMap analysisMap = AnalysisMap.getInstance();
    private Integer deposits = 0;

    @Override
    public Transaction process(Transaction item) throws Exception {
        if(Double.parseDouble(item.getAmount().replace("$", "")) < 0) {
            deposits++;
            //Integer deposits = (Integer) analysisMap.getReportMap().get("number-of-deposits") + 1;
            //analysisMap.getReportMap().put("number-of-deposits", deposits);
        }
        return null;
    }

    @Override
    public void afterProcess(Transaction I, Transaction O) {
        //System.out.println("Deposits: " + deposits);
    }
}
