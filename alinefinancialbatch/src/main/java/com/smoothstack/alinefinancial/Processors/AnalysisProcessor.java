package com.smoothstack.alinefinancial.Processors;

import com.smoothstack.alinefinancial.Maps.AnalysisMap;
import com.smoothstack.alinefinancial.Models.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

@Slf4j(topic = "AnalysisProcessor")
public class AnalysisProcessor implements ItemProcessor<Transaction, Transaction> {

    private final AnalysisMap analysisMap = AnalysisMap.getInstance();

    private Long transactionLine = 1L;

    @Override
    public Transaction process(Transaction item) throws Exception {

        try {

            try {
                synchronized (AnalysisProcessor.class) {
                    // NRVNA-88 Fraud by year
                    // counting number of times fraud appears for a year
                    if (item.getFraud().equals("Yes")) {
                        if (analysisMap.getFraudByYearMap().get(item.getYear()) == null) {
                            analysisMap.getFraudByYearMap().put(item.getYear(), 1L);
                        } else {
                            analysisMap.getFraudByYearMap().put(item.getYear(), analysisMap.getFraudByYearMap().get(item.getYear()) + 1);
                        }
                    }
                }
            } catch (Exception e) {
                StringBuilder errorMessage = new StringBuilder();
                errorMessage.append("fraudByYear\tException: ");
                errorMessage.append(e);
                log.error(errorMessage.toString());
            }

            try {
                // counting number of transactions per year
                synchronized (AnalysisProcessor.class) {
                    if (analysisMap.getTransactionsByYearMap().get(item.getYear()) == null) {
                        analysisMap.getTransactionsByYearMap().put(item.getYear(), 1L);
                    } else {
                        analysisMap.getTransactionsByYearMap().put(item.getYear(), analysisMap.getTransactionsByYearMap().get(item.getYear()) + 1);
                    }
                }
            } catch (Exception e) {
                StringBuilder errorMessage = new StringBuilder();
                errorMessage.append("transactionsByYear\tException: ");
                errorMessage.append(e);
                log.error(errorMessage.toString());
            }

            try {
                // NRVNA-89
                synchronized (AnalysisProcessor.class) {
                    if (!item.getMerchant_zip().equals("")) {
                        if (analysisMap.getZipTransactions().get(item.getMerchant_zip()) == null) {
                            Double amt = Double.parseDouble(item.getAmount().replace("$", ""));
                            analysisMap.getZipTransactions().put(item.getMerchant_zip(), amt);
                        } else {
                            Double amt = Double.parseDouble(item.getAmount().replace("$", ""));
                            analysisMap.getZipTransactions().put(item.getMerchant_zip(), analysisMap.getZipTransactions().get(item.getMerchant_zip()) + amt);
                        }
                    }
                }
            } catch (Exception e) {
                StringBuilder errorMessage = new StringBuilder();
                errorMessage.append("transactionsByZip\tException: ");
                errorMessage.append(e);
                log.error(errorMessage.toString());
            }

            try {
                // NRVNA-89 actual I think
                synchronized (AnalysisProcessor.class) {
                    if (!item.getMerchant_zip().equals("")) {
                        if (analysisMap.getZipTotalTransactions().get(item.getMerchant_zip()) == null) {
                            analysisMap.getZipTotalTransactions().put(item.getMerchant_zip(), 1L);
                        } else {
                            analysisMap.getZipTotalTransactions().put(item.getMerchant_zip(), analysisMap.getZipTotalTransactions().get(item.getMerchant_zip()) + 1);
                        }
                    }
                }
            } catch (Exception e) {
                StringBuilder errorMessage = new StringBuilder();
                errorMessage.append("realTransactionsByZip\tException: ");
                errorMessage.append(e);
                log.error(errorMessage.toString());
            }

            try {
                // NRVNA-90
                synchronized (AnalysisProcessor.class) {
                    if (!item.getMerchant_city().equals("")) {
                        if (analysisMap.getCitiesTotalTransactions().get(item.getMerchant_city()) == null) {
                            analysisMap.getCitiesTotalTransactions().put(item.getMerchant_city(), 1L);
                        } else {
                            analysisMap.getCitiesTotalTransactions().put(item.getMerchant_city(), analysisMap.getCitiesTotalTransactions().get(item.getMerchant_city()) + 1);
                        }
                    }
                }
            } catch (Exception e) {
                StringBuilder errorMessage = new StringBuilder();
                errorMessage.append("transactionsByCity\tException: ");
                errorMessage.append(e);
                log.error(errorMessage.toString());
            }

            try {
                // NRVNA-93 top 10 largest transactions
                synchronized (AnalysisProcessor.class) {
                    if(analysisMap.getLargestTransactions().size() < 10) {
                        Double amt = Double.parseDouble(item.getAmount().replace("$", ""));
                        analysisMap.getLargestTransactions().add(amt);
                        analysisMap.getLargestTransactions().sort(Comparator.reverseOrder());
                    } else {
                        Double amt = Double.parseDouble(item.getAmount().replace("$", ""));
                        if (amt > analysisMap.getLargestTransactions().get(9)) {
                            analysisMap.getLargestTransactions().remove(9);
                            analysisMap.getLargestTransactions().add(amt);
                            analysisMap.getLargestTransactions().sort(Comparator.reverseOrder());
                        }
                    }
                }
            } catch (Exception e) {
                StringBuilder errorMessage = new StringBuilder();
                errorMessage.append("top10Transactions\tException: ");
                errorMessage.append(e);
                log.error(errorMessage.toString());
            }

            try {
                synchronized (AnalysisProcessor.class) {
                    if (analysisMap.getTypesOfTransactions().get(item.getMethod()) == null) {
                        analysisMap.getTypesOfTransactions().put(item.getMethod(), 1L);
                    } else {
                        analysisMap.getTypesOfTransactions().put(item.getMethod(), analysisMap.getTypesOfTransactions().get(item.getMethod()) + 1);
                    }
                }
            } catch (Exception e) {
                StringBuilder errorMessage = new StringBuilder();
                errorMessage.append("typesOfTransactions\tException: ");
                errorMessage.append(e);
                log.error(errorMessage.toString());
            }

            // NRVNA-94 total transactions by state with no fraud
            try {
                // first time seeing state set up boolean hashmap
                if(analysisMap.getStatesNoFraud().get(item.getMerchant_state()) == null) {
                    analysisMap.getStatesNoFraud().put(item.getMerchant_state(), new HashMap<>());

                    if(item.getFraud().equals("Yes")) {
                        //first time seeing fraud in state
                        if(analysisMap.getStatesNoFraud().get(item.getMerchant_state()).get(true) == null) {
                            analysisMap.getStatesNoFraud().get(item.getMerchant_state()).put(true, 1L);
                        } else {
                            // seen fraud in state before
                            analysisMap.getStatesNoFraud().get(item.getMerchant_state()).put(true, analysisMap.getStatesNoFraud().get(item.getMerchant_state()).get(true) + 1);
                        }
                    } else {
                        // first time new state has no fraud
                        if(analysisMap.getStatesNoFraud().get(item.getMerchant_state()).get(false) == null) {
                            analysisMap.getStatesNoFraud().get(item.getMerchant_state()).put(false, 1L);
                        } else {
                            // state has not had fraud before
                            analysisMap.getStatesNoFraud().get(item.getMerchant_state()).put(false, analysisMap.getStatesNoFraud().get(item.getMerchant_state()).get(false) + 1);
                        }
                    }
                } else {

                    if(item.getFraud().equals("Yes")) {
                        if(analysisMap.getStatesNoFraud().get(item.getMerchant_state()).get(true) == null) {
                            analysisMap.getStatesNoFraud().get(item.getMerchant_state()).put(true, 1L);
                        } else {
                            analysisMap.getStatesNoFraud().get(item.getMerchant_state()).put(true, analysisMap.getStatesNoFraud().get(item.getMerchant_state()).get(true) + 1);
                        }
                    } else {
                        if(analysisMap.getStatesNoFraud().get(item.getMerchant_state()).get(false) == null) {
                            analysisMap.getStatesNoFraud().get(item.getMerchant_state()).put(false, 1L);
                        } else {
                            analysisMap.getStatesNoFraud().get(item.getMerchant_state()).put(false, analysisMap.getStatesNoFraud().get(item.getMerchant_state()).get(false) + 1);
                        }
                    }
                }
            } catch (Exception e) {
                StringBuilder errorMessage = new StringBuilder();
                errorMessage.append("statesNoFraud\tException: ");
                errorMessage.append(e);
                log.error(errorMessage.toString());
            }

            synchronized (AnalysisProcessor.class) {
                try {

                    Double amt = Double.parseDouble(item.getAmount().replace("$", ""));
                    LocalTime time = LocalTime.parse(item.getTime(), DateTimeFormatter.ofPattern("HH:mm"));
                    Integer hour = time.getHour();
                    Integer minutes = time.getMinute();
                    if((amt > 100 && hour > 20) || (amt > 100 && hour.equals(20) && minutes > 0)) {
                        if(item.getMerchant_city().equals("ONLINE")) {
                            if(analysisMap.getTransAfter8Above100().get("ONLINE") == null) {
                                analysisMap.getTransAfter8Above100().put("ONLINE", new ArrayList<>());
                                analysisMap.getTransAfter8Above100().get("ONLINE").add(item);
                            } else {
                                analysisMap.getTransAfter8Above100().get("ONLINE").add(item);
                            }
                        } else {
                            if(analysisMap.getTransAfter8Above100().get(item.getMerchant_zip()) == null) {
                                analysisMap.getTransAfter8Above100().put(item.getMerchant_zip(), new ArrayList<>());
                                analysisMap.getTransAfter8Above100().get(item.getMerchant_zip()).add(item);
                            } else {
                                analysisMap.getTransAfter8Above100().get(item.getMerchant_zip()).add(item);
                            }
                        }
                    }

                } catch (Exception e) {
                    StringBuilder errorMessage = new StringBuilder();
                    errorMessage.append("transAbove$100After8PM\tException: ");
                    errorMessage.append(e);
                    log.error(errorMessage.toString());
                }
            }

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
