package com.ooclass.bankapp.models;

public enum TransactionType {
    WITHDRAWAL(0, "Withdraw"),
    DEPOSIT(1, "Deposit"),
    TRANSFER(2, "Transfer"),
    CARD_PAYMENT(3, "Card payment");

    private int code;
    private String name;

    TransactionType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return this.name;
    }
}