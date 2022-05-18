package com.smoothstack.alinefinancial.enums;

public enum StatisticStrings {
    TOPTENLARGESTTRANSACTIONS("top-ten-largest-transactions"),
    BALANCES("insufficient-balance"),
    TOPFIVECITIESTRANSACTIONS("top-five-cities-total-transactions")
    ;

    private final String text;

    StatisticStrings(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
