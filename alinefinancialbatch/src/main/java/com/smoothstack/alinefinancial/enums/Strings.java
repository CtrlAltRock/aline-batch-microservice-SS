package com.smoothstack.alinefinancial.enums;

public enum Strings {
    YES("Yes"),
    NO("No"),
    ONLINE("ONLINE"),
    INSUFFICIENT("Insufficient Balance"),
    COMMA(",")
    ;

    private final String text;

    Strings(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
