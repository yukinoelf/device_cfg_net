package com.example.cfgnet;

public enum ErrorCode {
    OK(0),
    FAILTURE_SEND_WIFI(-1),
    FAILTURE_FIND_DEVICE(-2);

    private final int value;

    ErrorCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
