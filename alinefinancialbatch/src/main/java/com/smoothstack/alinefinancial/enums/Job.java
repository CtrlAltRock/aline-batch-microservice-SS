package com.smoothstack.alinefinancial.enums;

public enum Job {
    COMPLETED("COMPLETED"),
    FAILED("FAILED");

    private final String text;

    Job(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
