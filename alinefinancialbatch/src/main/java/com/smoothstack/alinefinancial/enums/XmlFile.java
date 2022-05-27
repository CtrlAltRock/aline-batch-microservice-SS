package com.smoothstack.alinefinancial.enums;

public enum XmlFile {

    FILEPATH("src/main/ProcessedOutFiles"),
    HEADER("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"),
    CARDS("Cards.xml"),
    USERS("Users.xml"),
    MERCHANTS("Merchants.xml"),
    STATES("States.xml"),
    DEPOSITS("UserDeposits.xml"),
    BALANCES("UsersInsufficientBalance.xml"),
    MERCHANTCOUNT("TotalUniqueMerchants.xml"),
    TOPZIPTRANSVOL("TopFiveZipTransVol.xml"),
    RECURRINGTRANSACTION("RecurringTransactions.xml"),
    TRANSACTIONAFTER8PMOVER100("TransactionsOver100And8PM.xml"),
    TYPESOFTRANSACTIONS("TypesOfTransactions.xml"),
    TOPTENLARGESTTRANSACTIONS("TopTenLargestTransactions.xml"),
    TOPFIVECITIESHIGHESTVOLUME("TopFiveHighestVolume.xml"),
    STATESNOFRAUD("StatesNoFraud.xml"),
    MONTHONLINECOUNT("LowestMonthOnlineTransactionCount.xml")
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
