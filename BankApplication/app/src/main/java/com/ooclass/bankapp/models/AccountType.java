package com.ooclass.bankapp.models;

public enum AccountType {
    CURRENT_ACCOUNT(0, "Current Account"),
    SAVINGS_ACCOUNT(1, "Savings Account"),
    BUSINESS_ACCOUNT(2, "Business Account");

    private int code;
    private String type;

    AccountType(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }
}
