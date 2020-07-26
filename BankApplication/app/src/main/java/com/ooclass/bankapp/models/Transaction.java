package com.ooclass.bankapp.models;

import java.util.Date;

public class Transaction {
    long transactionId;
    TransactionType type;
    String name; // used for naming card transactions
    Account fromAccount;
    Account toAccount;
    float amount;
    Date date;

    public Transaction() {
        this.name = "";
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Transaction(TransactionType type, Account fromAccount, Account toAccount, float amount, Date date) {
        this.type = type;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.date = date;
    }

    public static Transaction MakeTransaction(TransactionType type, Account fromAccount, Account toAccount, float amount) {
        return new Transaction(type, fromAccount, toAccount, amount, new Date());
    }

    public static Transaction MakeTransaction(String name, TransactionType type, Account fromAccount, Account toAccount, float amount) {
        Transaction transaction = new Transaction(type, fromAccount, toAccount, amount, new Date());
        transaction.setName(name);
        return transaction;
    }
}
