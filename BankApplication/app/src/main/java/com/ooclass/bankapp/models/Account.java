package com.ooclass.bankapp.models;

public class Account {
    long accountId;
    AccountType type;
    User owner;
    String name;
    float balance;
    boolean isPayable;
    boolean isTransferable;

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public boolean isPayable() {
        return isPayable;
    }

    public void setPayable(boolean payable) {
        isPayable = payable;
    }

    public boolean isTransferable() {
        return isTransferable;
    }

    public void setTransferable(boolean transferable) {
        isTransferable = transferable;
    }

    public Account() {
        this.owner = null;
        this.isPayable = false;
        this.isTransferable = false;
        this.balance = 0;
        this.type = AccountType.CURRENT_ACCOUNT;
    }

    public Account(long accountId, AccountType type, User owner, String name, float balance, boolean isPayable, boolean isTransferable) {
        this.accountId = accountId;
        this.type = type;
        this.owner = owner;
        this.name = name;
        this.balance = balance;
        this.isPayable = isPayable;
        this.isTransferable = isTransferable;
    }

    public void deposit(float amount) {
        balance += amount;
        Bank.makeTransaction(Transaction.MakeTransaction(owner.getFullName(), TransactionType.DEPOSIT, null, this, amount));
        Bank.saveAccount(this);
    }

    public void receiveTransaction(float amount) {
        balance += amount;
        Bank.saveAccount(this);
    }

    public void deductMoney(float amount) {
        balance -= amount;
        Bank.saveAccount(this);
    }

    public boolean isBalanceSufficient(float amount) {
        return balance >= amount;
    }

    public ActionResult transferMoneyTo(float amount, Account destination) {
        ActionResult result = new ActionResult();
        result.success = false;

        if (isBalanceSufficient(amount)) {
            Bank.makeTransaction(Transaction.MakeTransaction(TransactionType.TRANSFER, this, destination, amount));
            deductMoney(amount);
            destination.receiveTransaction(amount);

            result.success = true;
            result.message = "Payment successful";
        } else {
            result.message = "Insufficient balance.";
        }

        return result;
    }

    /* Transfers money between the users' accounts */
    public ActionResult transferPersonal(float amount, Account destination) {
        ActionResult result = new ActionResult();
        result.success = false;

        if (destination.owner == owner && isTransferable) {
            return transferMoneyTo(amount, destination);
        } else {
            result.message = "You cannot transfer money from this account";
        }

        return result;
    }

    /* Transfers money to an external account */
    public ActionResult transferExternal(float amount, String destinationTo) {
        ActionResult result = new ActionResult();
        result.success = false;

        if (isBalanceSufficient(amount)) {
            Bank.makeTransaction(Transaction.MakeTransaction(destinationTo, TransactionType.TRANSFER, this, null, amount));
            deductMoney(amount);

            result.success = true;
            result.message = "Payment successful!";
        } else {
            result.message = "Insufficient balance.";
        }

        return result;
    }

    /* Transfers money to another account within the Bank */
    public ActionResult transferInternal(float amount, Account destination) {
        ActionResult result = new ActionResult();
        result.success = false;

        if (isPayable) {
            result = transferMoneyTo(amount, destination);
        } else {
            result.message = "You cannot do payments on this account.";
        }

        return result;
    }
}
