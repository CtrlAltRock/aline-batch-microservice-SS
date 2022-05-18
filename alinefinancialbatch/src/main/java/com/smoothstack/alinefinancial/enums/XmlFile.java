package com.smoothstack.alinefinancial.enums;

public enum XmlFile {

    HEADER("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"),
    CARDS("XmlCards.xml"),
    USERS("XmlUsers.xml"),
    MERCHANTS("XmlMerchants.xml"),
    STATES("XmlStates.xml"),
    DEPOSITS("XmlUserDeposits.xml"),
    BALANCES("XmlUsersInsufficientBalance.xml"),
    MERCHANTCOUNT("XmlTotalUniqueMerchants.xml"),
    TRANSACTIONAFTER8PMOVER100("XmlTransactionsOver100And8PM.xml"),
    TOPTENLARGESTTRANSACTIONS("XmlTopTenLargestTransactions.xml")
    ;

    private final String text;

    XmlFile(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
