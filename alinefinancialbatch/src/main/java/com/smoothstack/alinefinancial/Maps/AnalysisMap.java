package com.smoothstack.alinefinancial.Maps;

import com.smoothstack.alinefinancial.Models.Transaction;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j(topic = "AnalysisMap")
public class AnalysisMap {
    private HashMap<String, Object> reportMap = new HashMap<>();
    private final HashMap<Integer, Long> fraudByYear = new HashMap<>();
    private final HashMap<Integer, Long> transactionsByYear = new HashMap<>();
    private final HashMap<String, Double> zipTransactions = new HashMap<>();
    private final HashMap<String, Long> zipTotalTransactions = new HashMap<>();
    private final HashMap<String, Long> citiesTotalTransactions = new HashMap<>();
    private final HashMap<String, Long> typesOfTransactions = new HashMap<>();
    private final HashMap<String, HashMap<Boolean, Long> >  statesNoFraud = new HashMap<>();
    private final HashMap<String, ArrayList<Transaction> > transAfter8Above100 = new HashMap<>();

    private  List<Double> largestTransactions = new ArrayList<>();



    private static AnalysisMap analysisMapInstance = null;

    public static AnalysisMap getInstance() {
        if(analysisMapInstance == null) {
            analysisMapInstance = new AnalysisMap();
        }
        return analysisMapInstance;
    }

    public synchronized HashMap<String, Object> getReportMap() {
        return reportMap;
    }

    public synchronized HashMap<Integer, Long> getTransactionsByYearMap() {return transactionsByYear;}

    public synchronized HashMap<Integer, Long> getFraudByYearMap() {return fraudByYear;}

    public synchronized HashMap<String, Double> getZipTransactions() {return zipTransactions;}

    public synchronized HashMap<String, Long> getZipTotalTransactions() {return zipTotalTransactions;}

    public synchronized HashMap<String, Long> getCitiesTotalTransactions() {return citiesTotalTransactions;}

    public synchronized HashMap<String, Long> getTypesOfTransactions() {return typesOfTransactions;}

    public synchronized HashMap<String, HashMap<Boolean, Long> > getStatesNoFraud() {return statesNoFraud;}

    public synchronized HashMap<String, ArrayList<Transaction> > getTransAfter8Above100() {return transAfter8Above100;}

    public synchronized List<Double> getLargestTransactions() {return largestTransactions;}

    public synchronized void addToLargestTransactions(Double amt) {

    }

    public void setStatistic(String statName, Object stat) {
        try {
            reportMap.put(statName, stat);
        } catch (Exception e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Method: setStatistic\tException: ");
            errorMessage.append(e);
            log.error(errorMessage.toString());
        }
    }
}
