package com.smoothstack.alinefinancial.enums;

public enum Errors {
    INSUFFICIENT("Insufficient Balance");


    private final String text;

    Errors(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
