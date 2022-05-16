package com.smoothstack.alinefinancial.enums;

public enum enums {
    YES("Yes"),
    NO("No"),
    ONLINE("ONLINE"),
    ;

    private final String text;

    enums(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
