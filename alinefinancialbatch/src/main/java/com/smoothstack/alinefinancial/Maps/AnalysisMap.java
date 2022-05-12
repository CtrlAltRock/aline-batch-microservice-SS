package com.smoothstack.alinefinancial.Maps;

import com.smoothstack.alinefinancial.DataAnalysisModels.UserDeposit;
import com.smoothstack.alinefinancial.Models.Merchant;
import com.smoothstack.alinefinancial.Models.Transaction;
import com.smoothstack.alinefinancial.Models.User;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.util.Objects.isNull;

@Slf4j(topic = "AnalysisMap")
public class AnalysisMap {
    private final HashMap<String, Object> reportMap = new HashMap<>();
    private final HashMap<Integer, Long> fraudByYear = new HashMap<>();
    private final HashMap<Integer, Long> transactionsByYear = new HashMap<>();
    private final HashMap<String, Double> zipTransactions = new HashMap<>();
    private final HashMap<String, Long> zipTotalTransactions = new HashMap<>();
    private final HashMap<String, Long> citiesTotalTransactions = new HashMap<>();
    private final HashMap<String, Long> typesOfTransactions = new HashMap<>();
    private final HashMap<User, Integer> insufficientBalance = new HashMap<>();
    private final HashMap<User, UserDeposit> userDeposits = new HashMap<>();
    private final HashMap<String, HashMap<Boolean, Long> >  statesNoFraud = new HashMap<>();
    private final HashMap<String, ArrayList<Transaction> > transAfter8Above100 = new HashMap<>();
    private final Map<Merchant, HashMap<Double, Integer> > transactionsByAmt = Collections.synchronizedMap(new HashMap<>());

    private  List<Double> largestTransactions = new ArrayList<>();



    private static AnalysisMap analysisMap = null;

    public static AnalysisMap getInstance() {
        if(analysisMap == null) {
            synchronized (AnalysisMap.class) {
                if(analysisMap == null) {
                    analysisMap = new AnalysisMap();
                }
            }
        }
        return analysisMap;
    }

    public HashMap<String, Object> getReportMap() {
        return reportMap;
    }

    public Map<Merchant, HashMap<Double, Integer> > getTransactionsByAmtByMerchant() {return transactionsByAmt;}

    public HashMap<User, Integer> getInsufficientBalanceMap() {return insufficientBalance;}

    public HashMap<Integer, Long> getTransactionsByYearMap() {return transactionsByYear;}

    public HashMap<Integer, Long> getFraudByYearMap() {return fraudByYear;}

    public HashMap<String, Double> getZipTransactions() {return zipTransactions;}

    public HashMap<String, Long> getZipTotalTransactions() {return zipTotalTransactions;}

    public HashMap<String, Long> getCitiesTotalTransactions() {return citiesTotalTransactions;}

    public HashMap<String, Long> getTypesOfTransactions() {return typesOfTransactions;}

    public HashMap<String, HashMap<Boolean, Long> > getStatesNoFraud() {return statesNoFraud;}

    public HashMap<String, ArrayList<Transaction> > getTransAfter8Above100() {return transAfter8Above100;}

    public HashMap<User, UserDeposit> getUserDeposit() {return userDeposits;}

    public List<Double> getLargestTransactions() {return largestTransactions;}

    // NRVNA - 86 Top five recurring transactions group by merchant
    public synchronized void addMerchantTransaction(Merchant merchant, String amount) {
        Double amt = Double.parseDouble(amount.replace("$", ""));
        // haven't seen merchant before
        if(isNull(transactionsByAmt.get(merchant))) {
            HashMap<Double, Integer> transactionsByFreq = new HashMap<>();
            transactionsByFreq.put(amt, 1);
            transactionsByAmt.put(merchant, transactionsByFreq);
        } else {
            // we've seen merchant before, but have we haven't seen this amt yet
            if(isNull(transactionsByAmt.get(merchant).get(amt))) {
                transactionsByAmt.get(merchant).put(amt, 1);
            } else {
                // we've seen this merchant before and we've seen this amt
                transactionsByAmt.get(merchant).put(amt, transactionsByAmt.get(merchant).get(amt) + 1);
            }
        }
    }

    // NRVNA - 83 deposits for users
    public synchronized void addToUserDeposits(User user, Transaction transaction) {
        if(isNull(userDeposits.get(user))) {
            List<Transaction> deposits = new ArrayList<Transaction>();
            deposits.add(transaction);
            UserDeposit userDeposit = new UserDeposit(user, deposits);
            userDeposits.put(user, userDeposit);
        } else {
            userDeposits.get(user).addDeposit(transaction);
        }
    }

    // NRVNA - 84 & 85 Insufficient balance for users at least once and more than once
    public synchronized void addToInsufficientBalance(User user) {
        if(isNull(insufficientBalance.get(user))) {
            insufficientBalance.put(user, 1);
        } else {
            insufficientBalance.put(user, insufficientBalance.get(user) + 1);
        }
    }

    public synchronized void addToFraudByYear(Integer year, String fraud) {
        if (fraud.equals("Yes")) {
            if (isNull(fraudByYear.get(year))) {
                fraudByYear.put(year, 1L);
            } else {
                fraudByYear.put(year, fraudByYear.get(year) + 1);
            }
        }
    }

    public synchronized void addToTransactionsByYear(Integer year) {
        if (isNull(transactionsByYear.get(year))) {
            transactionsByYear.put(year, 1L);
        } else {
            transactionsByYear.put(year, transactionsByYear.get(year) + 1);
        }
    }

    public synchronized void addToLargestTransactions(Double amt) {
        if(largestTransactions.size() < 10) {
            largestTransactions.add(amt);
            largestTransactions.sort(Comparator.reverseOrder());
        } else {
            if (amt > largestTransactions.get(9)) {
                largestTransactions.remove(9);
                largestTransactions.add(amt);
                largestTransactions.sort(Comparator.reverseOrder());
            }
        }
    }

    public synchronized void addToTypeFrequencies(String type) {
        if (isNull(typesOfTransactions.get(type))) {
            typesOfTransactions.put(type, 1L);
        } else {
            typesOfTransactions.put(type, typesOfTransactions.get(type) + 1);
        }
    }

    public synchronized void addCityTransactionFrequencies(String city) {
        if (!city.isBlank()) {
            if (isNull(citiesTotalTransactions.get(city))) {
                citiesTotalTransactions.put(city, 1L);
            } else {
                citiesTotalTransactions.put(city, citiesTotalTransactions.get(city) + 1);
            }
        }
    }

    public synchronized void addToZipTotals(String zip) {
        if (!zip.isBlank()) {
            if (isNull(zipTotalTransactions.get(zip))) {
                zipTotalTransactions.put(zip, 1L);
            } else {
                zipTotalTransactions.put(zip, zipTotalTransactions.get(zip) + 1);
            }
        }
    }

    public synchronized void addToStatesNoFraud(String state, String fraud) {
        // first time seeing state set up boolean hashmap
        if(isNull(statesNoFraud.get(state))) {
            statesNoFraud.put(state, new HashMap<>());

            if(fraud.equals("Yes")) {
                //first time seeing fraud in state
                if(isNull(statesNoFraud.get(state).get(true))) {
                    statesNoFraud.get(state).put(true, 1L);
                } else {
                    // seen fraud in state before
                    statesNoFraud.get(state).put(true, statesNoFraud.get(state).get(true) + 1);
                }
            } else {
                // first time new state has no fraud
                if(isNull(statesNoFraud.get(state).get(false))) {
                    statesNoFraud.get(state).put(false, 1L);
                } else {
                    // state has not had fraud before
                    statesNoFraud.get(state).put(false, statesNoFraud.get(state).get(false) + 1);
                }
            }
        } else {

            if(fraud.equals("Yes")) {
                if(isNull(statesNoFraud.get(state).get(true))) {
                    statesNoFraud.get(state).put(true, 1L);
                } else {
                    statesNoFraud.get(state).put(true, statesNoFraud.get(state).get(true) + 1);
                }
            } else {
                if(isNull(statesNoFraud.get(state).get(false))) {
                    statesNoFraud.get(state).put(false, 1L);
                } else {
                    statesNoFraud.get(state).put(false, statesNoFraud.get(state).get(false) + 1);
                }
            }
        }
    }

    public synchronized void addToTransOver100AndAfter8PM(Transaction item) {
        Double amt = Double.parseDouble(item.getAmount().replace("$", ""));
        LocalTime time = LocalTime.parse(item.getTime(), DateTimeFormatter.ofPattern("HH:mm"));
        Integer hour = time.getHour();
        Integer minutes = time.getMinute();
        if((amt > 100 && hour > 20) || (amt > 100 && hour.equals(20) && minutes > 0)) {
            if(item.getMerchant_city().equals("ONLINE")) {
                if(isNull(transAfter8Above100.get("ONLINE"))) {
                    transAfter8Above100.put("ONLINE", new ArrayList<>());
                    transAfter8Above100.get("ONLINE").add(item);
                } else {
                    transAfter8Above100.get("ONLINE").add(item);
                }
            } else {
                if(isNull(transAfter8Above100.get(item.getMerchant_zip()))) {
                    transAfter8Above100.put(item.getMerchant_zip(), new ArrayList<>());
                    transAfter8Above100.get(item.getMerchant_zip()).add(item);
                } else {
                    transAfter8Above100.get(item.getMerchant_zip()).add(item);
                }
            }
        }

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
