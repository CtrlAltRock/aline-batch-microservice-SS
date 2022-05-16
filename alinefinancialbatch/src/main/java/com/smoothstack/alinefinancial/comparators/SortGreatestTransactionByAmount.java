package com.smoothstack.alinefinancial.comparators;

import com.smoothstack.alinefinancial.models.Transaction;

import java.util.Comparator;

public class SortGreatestTransactionByAmount implements Comparator<Transaction> {

    @Override
    public int compare(Transaction o1, Transaction o2) {
        Double amt1 = Double.parseDouble(o1.getAmount().replace("$", ""));
        Double amt2 = Double.parseDouble(o2.getAmount().replace("$", ""));
        return Double.compare(amt2, amt1);
    }
}
