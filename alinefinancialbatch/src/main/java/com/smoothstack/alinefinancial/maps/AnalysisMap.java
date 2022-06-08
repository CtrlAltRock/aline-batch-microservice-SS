package com.smoothstack.alinefinancial.maps;

import com.smoothstack.alinefinancial.comparators.SortGreatestTransactionByAmount;
import com.smoothstack.alinefinancial.dto.RecurringTransaction;
import com.smoothstack.alinefinancial.dto.UserDeposit;
import com.smoothstack.alinefinancial.enums.Strings;
import com.smoothstack.alinefinancial.models.Merchant;
import com.smoothstack.alinefinancial.models.Transaction;
import com.smoothstack.alinefinancial.models.User;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static java.util.Objects.isNull;

@Slf4j(topic = "AnalysisMap")
public class AnalysisMap {

    private final HashMap<String, Object> reportMap = new HashMap<>();
    private final HashMap<Integer, Long> fraudByYear = new HashMap<>();
    private final HashMap<Integer, Long> transactionsByYear = new HashMap<>();
    private final HashMap<String, Long> zipTotalTransactions = new HashMap<>();
    private final HashMap<String, Long> citiesTotalTransactions = new HashMap<>();
    private final HashMap<String, Long> typesOfTransactions = new HashMap<>();
    private final HashMap<User, Integer> insufficientBalance = new HashMap<>();
    private final HashMap<Long, UserDeposit> userDeposits = new HashMap<>();
    private final HashMap<String, HashMap<Boolean, Long> >  statesNoFraud = new HashMap<>();
    private final HashMap<String, ArrayList<Transaction> > transAfter8Above100 = new HashMap<>();
    private final HashMap<RecurringTransaction, Long> recurringTransactions = new HashMap<>();
    private final HashMap<Integer, Long> monthsOnlineTransactionsCount = new HashMap<>();
    private final HashMap<Merchant, Long> merchantsWithInsufficientBalanceErrors = new HashMap<>();
    private final List<Transaction> largestTransactions = new ArrayList<>();

    private final MerchantMap merchantMap = MerchantMap.getInstance();

    private static AnalysisMap analysisMap = null;

    public static AnalysisMap getAnalysisMap() {
        if(analysisMap == null) {
            synchronized (AnalysisMap.class) {
                if(analysisMap == null) {
                    analysisMap = new AnalysisMap();
                }
            }
        }
        return analysisMap;
    }

    // HashMap getters
    public HashMap<String, Object> getReportMap() {
        return reportMap;
    }

    public HashMap<RecurringTransaction, Long> getRecurringTransactions() {return recurringTransactions;}

    public HashMap<User, Integer> getInsufficientBalanceMap() {return insufficientBalance;}

    public HashMap<Integer, Long> getTransactionsByYearMap() {return transactionsByYear;}

    public HashMap<Integer, Long> getFraudByYearMap() {return fraudByYear;}

    public HashMap<String, Long> getZipTotalTransactions() {return zipTotalTransactions;}

    public HashMap<String, Long> getCitiesTotalTransactions() {return citiesTotalTransactions;}

    public HashMap<String, Long> getTypesOfTransactions() {return typesOfTransactions;}

    public HashMap<String, HashMap<Boolean, Long> > getStatesNoFraud() {return statesNoFraud;}

    public HashMap<String, ArrayList<Transaction> > getTransAfter8Above100() {return transAfter8Above100;}

    public HashMap<Long, UserDeposit> getUserDeposit() {return userDeposits;}

    public List<Transaction> getLargestTransactions() {return largestTransactions;}

    public HashMap<Integer, Long> getMonthsOnlineTransactionsCount() {return monthsOnlineTransactionsCount;}

    public HashMap<Merchant, Long> getMerchantsWithInsufficientBalanceErrors() {return merchantsWithInsufficientBalanceErrors;}


    // NRVNA - 86 Top five recurring transactions group by merchant
    public synchronized void addRecurringTransaction(RecurringTransaction item) {
        Timer.Sample timer = Timer.start(Metrics.globalRegistry);
        if(isNull(recurringTransactions.get(item))) {
            recurringTransactions.put(item, 1L);
        } else {
          recurringTransactions.put(item, recurringTransactions.get(item) + 1);
        }
        timer.stop(Timer.builder("add-recurring-transactions").register(Metrics.globalRegistry));
    }

    // NRVNA - 83 deposits for users
    public synchronized void addToUserDeposits(User user, Transaction transaction) {
        Timer.Sample timer = Timer.start(Metrics.globalRegistry);
        if(isNull(userDeposits.get(user.getId()))) {
            List<Transaction> deposits = new ArrayList<Transaction>();
            deposits.add(transaction);
            UserDeposit userDeposit = new UserDeposit(user, deposits);
            userDeposits.put(user.getId(), userDeposit);
        } else {
            userDeposits.get(user.getId()).addDeposit(transaction);
        }
        timer.stop(Timer.builder("add-to-users-deposits").register(Metrics.globalRegistry));
    }

    // NRVNA - 84 & 85 Insufficient balance for users at least once and more than once
    public synchronized void addToInsufficientBalance(User user) {
        Timer.Sample timer = Timer.start(Metrics.globalRegistry);
        if(isNull(insufficientBalance.get(user))) {
            insufficientBalance.put(user, 1);
        } else {
            insufficientBalance.put(user, insufficientBalance.get(user) + 1);
        }
        timer.stop(Timer.builder("add-to-insufficient-balance").register(Metrics.globalRegistry));
    }

    // NRVNA - 88 Percentage of fraud by year
    public synchronized void addToFraudByYear(Integer year, String fraud) {
        Timer.Sample timer = Timer.start(Metrics.globalRegistry);
        if (fraud.equals(Strings.YES.toString())) {
            if (isNull(fraudByYear.get(year))) {
                fraudByYear.put(year, 1L);
            } else {
                fraudByYear.put(year, fraudByYear.get(year) + 1);
            }
        }
        else {
            if (isNull(fraudByYear.get(year))) {
                fraudByYear.put(year, 0L);
            }
        }
        timer.stop(Timer.builder("add-to-fraud-by-year").register(Metrics.globalRegistry));
    }

    // divisors for percentage of fraud
    public synchronized void addToTransactionsByYear(Integer year) {
        Timer.Sample timer = Timer.start(Metrics.globalRegistry);
        if (isNull(transactionsByYear.get(year))) {
            transactionsByYear.put(year, 1L);
        } else {
            transactionsByYear.put(year, transactionsByYear.get(year) + 1);
        }
        timer.stop(Timer.builder("add-to-transactions-by-year").register(Metrics.globalRegistry));
    }

    // NRVNA - 93 Top 10 largest transactions
    public synchronized void addToLargestTransactions(Transaction item) {
        Timer.Sample timer = Timer.start(Metrics.globalRegistry);
        if(largestTransactions.size() < 10) {
            largestTransactions.add(item);
            Collections.sort(largestTransactions,new SortGreatestTransactionByAmount());
        } else {
            if (new SortGreatestTransactionByAmount().compare(item, largestTransactions.get(9)) > 0) {
                largestTransactions.remove(9);
                largestTransactions.add(item);
                Collections.sort(largestTransactions, new SortGreatestTransactionByAmount());

            }
        }
        timer.stop(Timer.builder("add-to-largest-transactions").register(Metrics.globalRegistry));
    }

    // NRVNA - 91 Identify all types of transactions
    public synchronized void addToTypeFrequencies(String type) {
        Timer.Sample timer = Timer.start(Metrics.globalRegistry);
        if (isNull(typesOfTransactions.get(type))) {
            typesOfTransactions.put(type, 1L);
        } else {
            typesOfTransactions.put(type, typesOfTransactions.get(type) + 1);
        }
        timer.stop(Timer.builder("add-to-type-frequencies").register(Metrics.globalRegistry));
    }

    // NRVNA - 90 Top 5 group by cities with total number of transactions
    public synchronized void addCityTransactionFrequencies(String city) {
        Timer.Sample timer = Timer.start(Metrics.globalRegistry);
        if (!city.isBlank()) {
            if (isNull(citiesTotalTransactions.get(city))) {
                citiesTotalTransactions.put(city, 1L);
            } else {
                citiesTotalTransactions.put(city, citiesTotalTransactions.get(city) + 1);
            }
        }
        timer.stop(Timer.builder("add-city-transaction-frequencies").register(Metrics.globalRegistry));
    }

    // NRVNA - 89 Top 5 group by zipcodes with total amount of transactions
    public synchronized void addToZipTotals(String zip) {
        Timer.Sample timer = Timer.start(Metrics.globalRegistry);
        if (!zip.isBlank()) {
            if (isNull(zipTotalTransactions.get(zip))) {
                zipTotalTransactions.put(zip, 1L);
            } else {
                zipTotalTransactions.put(zip, zipTotalTransactions.get(zip) + 1);
            }
        }
        timer.stop(Timer.builder("add-to-zip-totals").register(Metrics.globalRegistry));
    }

    // NRVNA - 94 Total transactions group by state that had no fraud
    public synchronized void addToStatesNoFraud(String state, String fraud) {
        Timer.Sample timer = Timer.start(Metrics.globalRegistry);
        if(!state.isBlank()) {
            // first time seeing state set up boolean hashmap
            if (isNull(statesNoFraud.get(state))) {
                statesNoFraud.put(state, new HashMap<>());

                if (fraud.equals(Strings.YES.toString())) {
                    //first time seeing fraud in state
                    if (isNull(statesNoFraud.get(state).get(true))) {
                        statesNoFraud.get(state).put(true, 1L);
                    } else {
                        // seen fraud in state before
                        statesNoFraud.get(state).put(true, statesNoFraud.get(state).get(true) + 1);
                    }
                } else {
                    // first time new state has no fraud
                    if (isNull(statesNoFraud.get(state).get(false))) {
                        statesNoFraud.get(state).put(false, 1L);
                    } else {
                        // state has not had fraud before
                        statesNoFraud.get(state).put(false, statesNoFraud.get(state).get(false) + 1);
                    }
                }
            } else {

                if (fraud.equals(Strings.YES.toString())) {
                    if (isNull(statesNoFraud.get(state).get(true))) {
                        statesNoFraud.get(state).put(true, 1L);
                    } else {
                        statesNoFraud.get(state).put(true, statesNoFraud.get(state).get(true) + 1);
                    }
                } else {
                    if (isNull(statesNoFraud.get(state).get(false))) {
                        statesNoFraud.get(state).put(false, 1L);
                    } else {
                        statesNoFraud.get(state).put(false, statesNoFraud.get(state).get(false) + 1);
                    }
                }
            }
        }
        timer.stop(Timer.builder("add-to-states-no-fraud").register(Metrics.globalRegistry));
    }

    public synchronized void addToTransOver100AndAfter8PM(Transaction item) {
        Timer.Sample timer = Timer.start(Metrics.globalRegistry);
        Double amt = Double.parseDouble(item.getAmount().replace("$", ""));
        LocalTime time = LocalTime.parse(item.getTime(), DateTimeFormatter.ofPattern("HH:mm"));
        Integer hour = time.getHour();
        Integer minutes = time.getMinute();
        if((amt > 100 && hour > 20) || (amt > 100 && hour.equals(20) && minutes > 0)) {
            if(item.getMerchant_city().equals(Strings.ONLINE.toString())) {
                if(isNull(transAfter8Above100.get(Strings.ONLINE.toString()))) {
                    transAfter8Above100.put(Strings.ONLINE.toString(), new ArrayList<>());
                    transAfter8Above100.get(Strings.ONLINE.toString()).add(item);
                } else {
                    transAfter8Above100.get(Strings.ONLINE.toString()).add(item);
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
        timer.stop(Timer.builder("add-to-trans-over-100-after-8pm").register(Metrics.globalRegistry));
    }


    // NRVNA - 96 Bottom 5 months with number of online transactions
    public synchronized void addToMonthsOnlineTransactionCount(Integer month) {
        Timer.Sample timer = Timer.start(Metrics.globalRegistry);
        if(isNull(monthsOnlineTransactionsCount.get(month))) {
            monthsOnlineTransactionsCount.put(month, 1L);
        } else {
            monthsOnlineTransactionsCount.put(month, monthsOnlineTransactionsCount.get(month) + 1);
        }
        timer.stop(Timer.builder("add-to-months-online-transaction-count").register(Metrics.globalRegistry));
    }

    // NRVNA - 97 Group by Top 5 merchants with insufficient balance that had no errors
    public synchronized void addToMerchantsWithInsufficientBalancesNoErrors(Transaction item) {
        Timer.Sample timer = Timer.start(Metrics.globalRegistry);

        if(isNull(merchantsWithInsufficientBalanceErrors.get(item.getMerchant_name()))) {
            if(!item.getErrors().isBlank()) {
                if(item.getErrors().contains(Strings.INSUFFICIENT.toString()) && item.getErrors().split(Strings.COMMA.toString()).length == 1) {
                    if(isNull(merchantsWithInsufficientBalanceErrors.get(merchantMap.getGeneratedMerchant(item.getMerchant_name())))) {
                        merchantsWithInsufficientBalanceErrors.put(merchantMap.getGeneratedMerchant(item.getMerchant_name()), 1L);
                    } else {
                        merchantsWithInsufficientBalanceErrors.put(merchantMap.getGeneratedMerchant(item.getMerchant_name()),
                                merchantsWithInsufficientBalanceErrors.get(merchantMap.getGeneratedMerchant(item.getMerchant_name())) + 1);
                    }
                }
            }
        }

        timer.stop(Timer.builder("add-to-merchants-with-insufficient-balances-no-errors").register(Metrics.globalRegistry));
    }

    public void setStatistic(String statName, Object stat) {
        Timer.Sample timer = Timer.start(Metrics.globalRegistry);
        reportMap.put(statName, stat);
        timer.stop(Timer.builder("set-statistic").register(Metrics.globalRegistry));
    }

    public static void resetAllMaps() {
        analysisMap = new AnalysisMap();
    }
}
