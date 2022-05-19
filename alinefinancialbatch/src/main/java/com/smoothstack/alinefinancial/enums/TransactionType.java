package com.smoothstack.alinefinancial.enums;

public enum TransactionType {
    ONLINE("Online Transaction"),
    SWIPE("Swipe Transaction"),
    CHIP("Chip Transaction");

    private final String text;

    TransactionType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
