package com.smoothstack.alinefinancial.processors;

import com.smoothstack.alinefinancial.maps.AnalysisMap;
import com.smoothstack.alinefinancial.models.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j(topic = "AnalysisProcessor")
public class AnalysisProcessor implements ItemProcessor<Transaction, Transaction> {

    private final AnalysisMap analysisMap = AnalysisMap.getInstance();

    private Long transactionLine = 1L;

    @Override
    public Transaction process(Transaction item) throws Exception {

        try {
            // NRVNA-88 Fraud by year
            // counting number of times fraud appears for a year
            analysisMap.addToFraudByYear(item.getYear(), item.getFraud());

        } catch (Exception e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("fraudByYear\tException: ");
            errorMessage.append(e);
            errorMessage.append(" on transaction line: ");
            errorMessage.append(transactionLine);
            log.error(errorMessage.toString());
        }

        try {
            // counting number of transactions per year
            analysisMap.addToTransactionsByYear(item.getYear());

        } catch (Exception e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("transactionsByYear\tException: ");
            errorMessage.append(e);
            errorMessage.append(" on transaction line: ");
            errorMessage.append(transactionLine);
            log.error(errorMessage.toString());
        }

        try {
            // NRVNA-89 Top 5 group by zipcodes with total amount of transactions
            analysisMap.addToZipTotals(item.getMerchant_zip());

        } catch (Exception e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("transactionsByZip\tException: ");
            errorMessage.append(e);
            errorMessage.append(" on transaction line: ");
            errorMessage.append(transactionLine);
            log.error(errorMessage.toString());
        }

        try {
            // NRVNA-90 Top 5 group by cities with total number of transactions
            analysisMap.addCityTransactionFrequencies(item.getMerchant_city());

        } catch (Exception e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("transactionsByCity\tException: ");
            errorMessage.append(e);
            errorMessage.append(" on transaction line: ");
            errorMessage.append(transactionLine);
            log.error(errorMessage.toString());
        }

        try {
            // NRVNA-93 top 10 largest transactions
            analysisMap.addToLargestTransactions(item);

        } catch (Exception e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("top10Transactions\tException: ");
            errorMessage.append(e);
            errorMessage.append(" on transaction line: ");
            errorMessage.append(transactionLine);
            log.error(errorMessage.toString());
        }

        //NRVNA-91 record all types of transactions and their frequencies
        try {
            analysisMap.addToTypeFrequencies(item.getMethod());

        } catch (Exception e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("typesOfTransactions\tException: ");
            errorMessage.append(e);
            errorMessage.append(" on transaction line: ");
            errorMessage.append(transactionLine);
            log.error(errorMessage.toString());
        }

        // NRVNA-94 total transactions by state with no fraud
        try {
            analysisMap.addToStatesNoFraud(item.getMerchant_state(), item.getFraud());

        } catch (Exception e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("statesNoFraud\tException: ");
            errorMessage.append(e);
            errorMessage.append(" on transaction line: ");
            errorMessage.append(transactionLine);
            log.error(errorMessage.toString());
        }

        // NRVNA-92 All transactions above $100 occuring after 8 PM group by zip code and online
        try {
            analysisMap.addToTransOver100AndAfter8PM(item);

        } catch (Exception e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("transAbove$100After8PM\tException: ");
            errorMessage.append(e);
            errorMessage.append(" on transaction line: ");
            errorMessage.append(transactionLine);
            log.error(errorMessage.toString());
        }

        transactionLine++;

        return item;
    }
}
