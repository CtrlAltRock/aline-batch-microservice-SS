package com.smoothstack.alinefinancial.processors;

import com.smoothstack.alinefinancial.enums.Strings;
import com.smoothstack.alinefinancial.maps.AnalysisMap;
import com.smoothstack.alinefinancial.models.Transaction;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j(topic = "AnalysisProcessor")
public class AnalysisProcessor implements ItemProcessor<Transaction, Transaction> {

    private final AnalysisMap analysisMap = AnalysisMap.getAnalysisMap();

    private Long transactionLine = 1L;

    private Long timesProcessed = 1L;

    @Override
    public Transaction process(Transaction item) throws Exception {
        Timer.Sample timer = Timer.start(Metrics.globalRegistry);
        // NRVNA-88 Fraud by year
        // counting number of times fraud appears for a year
        analysisMap.addToFraudByYear(item.getYear(), item.getFraud());


        // counting number of transactions per year
        analysisMap.addToTransactionsByYear(item.getYear());


        // NRVNA-89 Top 5 group by zipcodes with total amount of transactions
        analysisMap.addToZipTotals(item.getMerchant_zip());


        // NRVNA-90 Top 5 group by cities with total number of transactions
        if (!item.getMerchant_city().equals(Strings.ONLINE.toString())) {
            analysisMap.addCityTransactionFrequencies(item.getMerchant_city());
        } else {
            analysisMap.addToMonthsOnlineTransactionCount(item.getMonth());
        }


        // NRVNA-93 top 10 largest transactions
        analysisMap.addToLargestTransactions(item);


        //NRVNA-91 record all types of transactions and their frequencies
        analysisMap.addToTypeFrequencies(item.getMethod());


        // NRVNA-94 total transactions by state with no fraud
        analysisMap.addToStatesNoFraud(item.getMerchant_state(), item.getFraud());


        // NRVNA-92 All transactions above $100 occuring after 8 PM group by zip code and online
        analysisMap.addToTransOver100AndAfter8PM(item);

        transactionLine++;
        timer.stop(Timer.builder("analysis-processor").register(Metrics.globalRegistry));

        return item;
    }
}
