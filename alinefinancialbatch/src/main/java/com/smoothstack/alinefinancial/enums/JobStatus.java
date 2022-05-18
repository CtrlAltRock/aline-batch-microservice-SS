package com.smoothstack.alinefinancial.enums;

public enum JobStatus {
    COMPLETED("COMPLETED"),
    FAILED("FAILED");

    private final String text;

    JobStatus(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
