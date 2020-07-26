package com.ooclass.bankapp.models;

import android.os.AsyncTask;

import com.ooclass.bankapp.application.BankApplication;
import com.ooclass.bankapp.database.AppDatabase;
import com.ooclass.bankapp.database.transformers.AccountTransformer;
import com.ooclass.bankapp.database.transformers.BankCardTransformer;
import com.ooclass.bankapp.database.transformers.TransactionTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public final class Bank {
    /* Insert new account in database */
    public static boolean createAccount(String accountName, AccountType type, User user) {
        try {
            final Account account = new Account();
            account.setOwner(user);
            account.setName(accountName);
            account.setType(type);

            Long result = new AsyncTask<Void, Void, Long>() {
                @Override
                protected Long doInBackground(Void... voids) {
                    return AppDatabase.getInstance(BankApplication.getAppContext()).accountDao().insert(AccountTransformer.transform(account, true));
                }
            }.execute().get();

            // Successful insertion
            if (result >= 0) {
                account.setAccountId(result);
                // Cache new account
                BankApplication.accounts.add(account);
                return true;
            } else {
                return false;
            }
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
    }

    /* Returns all of the accounts of the user */
    public static List<Account> getUserAccounts(User user) {
        List<Account> accounts = BankApplication.accounts;
        List<Account> userAccounts = new ArrayList<Account>();

        for (Account account : accounts) {
            if (account.owner == user) {
                userAccounts.add(account);
            }
        }

        return userAccounts;
    }

    /* Returns all of the accounts of the user */
    public static List<BankCard> getUserBankCards(User user) {
        List<BankCard> bankCards = BankApplication.bankCards;
        List<BankCard> userBankCards = new ArrayList<BankCard>();

        for (BankCard bankCard : bankCards) {
            if (bankCard.getLinkedAccount().getOwner() == user) {
                userBankCards.add(bankCard);
            }
        }

        return userBankCards;
    }

    /* Returns all of the transactions of the user */
    public static List<Transaction> getUserTransactions(User user) {
        List<Transaction> transactions = BankApplication.transactions;
        List<Transaction> userTransactions = new ArrayList<Transaction>();

        for (Transaction transaction : transactions) {
            Account fromAccount = transaction.getFromAccount();
            Account toAccount = transaction.getToAccount();

            // Transfer between own accounts, separate these
            if ((toAccount != null && toAccount.getOwner() == user) && (fromAccount != null && fromAccount.getOwner() == user)) {
                userTransactions.add(Transaction.MakeTransaction(transaction.getFromAccount().getOwner().getFullName(),
                        TransactionType.TRANSFER, transaction.getFromAccount(), null, transaction.getAmount()));

                userTransactions.add(Transaction.MakeTransaction(transaction.getFromAccount().getOwner().getFullName(), TransactionType.TRANSFER,
                        null, transaction.getToAccount(), transaction.getAmount()));
            } else if ((toAccount != null && toAccount.getOwner() == user) || (fromAccount != null && fromAccount.getOwner() == user)) {
                userTransactions.add(transaction);
            }
        }

        return userTransactions;
    }

    /* Insert new transaction in database */
    public static boolean makeTransaction(final Transaction transaction) {
        try {
            Long result = new AsyncTask<Void, Void, Long>() {
                @Override
                protected Long doInBackground(Void... voids) {
                    return AppDatabase.getInstance(BankApplication.getAppContext()).transactionDao().insert(TransactionTransformer.transform(transaction, true));
                }
            }.execute().get();

            // Successful insertion
            if (result >= 0) {
                transaction.setTransactionId(result);
                // Cache new account
                BankApplication.transactions.add(transaction);
                return true;
            } else {
                return false;
            }
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
    }

    /* Save account in database */
    public static void saveAccount(final Account account) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                AppDatabase.getInstance(BankApplication.getAppContext()).accountDao().update(AccountTransformer.transform(account, false));
                return null;
            }
        }.execute();
    }

    /* Save bank card in database */
    public static void saveBankCard(final BankCard bankCard) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                AppDatabase.getInstance(BankApplication.getAppContext()).bankCardDao().update(BankCardTransformer.transform(bankCard, false));
                return null;
            }
        }.execute();
    }

    /* Insert new bank card in database */
    public static boolean createBankCard(Account account) {
        try {
            final BankCard bankCard = new BankCard();
            bankCard.setLinkedAccount(account);

            Long result = new AsyncTask<Void, Void, Long>() {
                @Override
                protected Long doInBackground(Void... voids) {
                    return AppDatabase.getInstance(BankApplication.getAppContext()).bankCardDao().insert(BankCardTransformer.transform(bankCard, true));
                }
            }.execute().get();

            // Successful insertion
            if (result >= 0) {
                bankCard.setBankCardId(result);
                // Cache new account
                BankApplication.bankCards.add(bankCard);
                return true;
            } else {
                return false;
            }
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
    }
}
