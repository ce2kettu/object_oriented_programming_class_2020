package com.ooclass.bankapp.models;

import java.util.ArrayList;
import java.util.List;

public class BankCard {
    long bankCardId;
    Account linkedAccount;
    String name;
    float withdrawalLimit;
    float payLimit;
    List<Country> allowedCountries;

    public BankCard() {
        withdrawalLimit = 500;
        payLimit = 500;
        name = "Visa Electron";
        allowedCountries = new ArrayList<Country>();
    }

    public long getBankCardId() {
        return bankCardId;
    }

    public void setBankCardId(long bankCardId) {
        this.bankCardId = bankCardId;
    }

    public Account getLinkedAccount() {
        return linkedAccount;
    }

    public void setLinkedAccount(Account linkedAccount) {
        this.linkedAccount = linkedAccount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getWithdrawalLimit() {
        return withdrawalLimit;
    }

    public void setWithdrawalLimit(float withdrawalLimit) {
        this.withdrawalLimit = withdrawalLimit;
    }

    public float getPayLimit() {
        return payLimit;
    }

    public void setPayLimit(float payLimit) {
        this.payLimit = payLimit;
    }

    public List<Country> getAllowedCountries() {
        return allowedCountries;
    }

    public void setAllowedCountries(List<Country> allowedCountries) {
        this.allowedCountries = allowedCountries;
    }

    /* Withdraws money from the linked account */
    public ActionResult withdrawInCountry(float amount, Country country) {
        ActionResult result = new ActionResult();
        result.success = false;

        if (linkedAccount.isBalanceSufficient(amount)) {
            if (amount <= withdrawalLimit) {
                if (allowedCountries.contains(country)) {
                    Bank.makeTransaction(Transaction.MakeTransaction("Bank", TransactionType.WITHDRAWAL, linkedAccount, null, amount));
                    linkedAccount.deductMoney(amount);

                    result.success = true;
                    result.message = "Withdrawal successful";
                } else {
                    result.message = "Card not allowed in this country";
                }
            } else {
                result.message = "Exceeded withdrawal limit";
            }
        } else {
            result.message = "Insufficient balance";
        }

        return result;
    }

    /* Card transaction */
    public ActionResult payInCountry(String destination, float amount, Country country) {
        ActionResult result = new ActionResult();
        result.success = false;

        if (linkedAccount.isBalanceSufficient(amount)) {
            if (amount <= payLimit) {
                if (allowedCountries.contains(country)) {
                    Bank.makeTransaction(Transaction.MakeTransaction(destination, TransactionType.CARD_PAYMENT, linkedAccount, null, amount));
                    linkedAccount.deductMoney(amount);

                    result.success = true;
                    result.message = "Payment successful";
                } else {
                    result.message = "Card not allowed in this country";
                }
            } else {
                result.message = "Exceeded pay limit";
            }
        } else {
            result.message = "Insufficient balance";
        }

        return result;
    }
}