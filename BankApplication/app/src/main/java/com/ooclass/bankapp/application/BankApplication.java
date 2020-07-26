package com.ooclass.bankapp.application;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ooclass.bankapp.database.AppDatabase;
import com.ooclass.bankapp.database.entities.AccountEntity;
import com.ooclass.bankapp.database.entities.BankCardEntity;
import com.ooclass.bankapp.database.entities.TransactionEntity;
import com.ooclass.bankapp.database.entities.UserEntity;
import com.ooclass.bankapp.database.transformers.AccountTransformer;
import com.ooclass.bankapp.database.transformers.BankCardTransformer;
import com.ooclass.bankapp.database.transformers.TransactionTransformer;
import com.ooclass.bankapp.database.transformers.UserTransformer;
import com.ooclass.bankapp.models.Account;
import com.ooclass.bankapp.models.BankCard;
import com.ooclass.bankapp.models.Transaction;
import com.ooclass.bankapp.models.User;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BankApplication extends Application {
    private static BankApplication instance;
    private static Context appContext;
    private static User currentUser = null;
    private static Account selectedAccount = null;
    private static BankCard selectedBankCard = null;

    public static List<User> users = new ArrayList<User>();
    public static List<Account> accounts = new ArrayList<Account>();
    public static List<Transaction> transactions = new ArrayList<Transaction>();
    public static List<BankCard> bankCards = new ArrayList<BankCard>();

    public static BankApplication getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return appContext;
    }

    public void setAppContext(Context mAppContext) {
        this.appContext = mAppContext;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setSelectedAccount(Account account) {
        this.selectedAccount = account;
    }

    public Account getSelectedAccount() {
        return this.selectedAccount;
    }

    public void setSelectedBankCard(BankCard bankCard) {
        this.selectedBankCard = bankCard;
    }

    public BankCard getSelectedBankCard() {
        return this.selectedBankCard;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        this.setAppContext(getApplicationContext());

        // Order is important
        loadUsers();
        loadAccounts();
        loadTransactions();
        loadBankCards();

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    /* Save all application data to a json file */
    public static void saveDataToStorage() {
        try {
            List<Object> data = new ArrayList<Object>();
            data.add(UserTransformer.transformListToEntities(users));
            data.add(TransactionTransformer.transformListToEntities(transactions));
            data.add(BankCardTransformer.transformListToEntities(bankCards));
            data.add(AccountTransformer.transformListToEntities(accounts));

            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            FileOutputStream fos = getAppContext().openFileOutput("saved_data.json", Context.MODE_PRIVATE);
            fos.write(gson.toJson(data).getBytes());
            fos.close();
        } catch (IOException e) {
        }
    }

    /* Load users from database */
    public static void loadUsers() {
        try {
            List<UserEntity> userList = new AsyncTask<Void, Void, List<UserEntity>>() {
                @Override
                protected List<UserEntity> doInBackground(Void... voids) {
                    List<UserEntity> users = AppDatabase.getInstance(appContext).userDao().getAll();
                    return users;
                }
            }.execute().get();

            users = UserTransformer.transform(userList);
        } catch (ExecutionException | InterruptedException e) {
        }
    }

    /* Map accounts owner to an object instance */
    private static void mapAccountOwner(AccountEntity entity, Account account) {
        for (User user : users) {
            if (entity.ownerId == user.getUserId()) {
                account.setOwner(user);
                return;
            }
        }
    }

    /* Map bank card's account to an object instance */
    private static void mapAccountForBankCard(BankCardEntity entity, BankCard bankCard) {
        for (Account account : accounts) {
            if (entity.linkedAccountId == account.getAccountId()) {
                bankCard.setLinkedAccount(account);
                return;
            }
        }
    }

    /* Map transaction's accounts to object instances */
    private static void mapAccountsForTransaction(TransactionEntity entity, Transaction transaction) {
        boolean condOneSatisfied = false;
        boolean condTwoSatisfied = false;

        for (Account account : accounts) {
            if (condOneSatisfied && condTwoSatisfied) {
                return;
            }

            if (entity.fromId == account.getAccountId() && !condOneSatisfied) {
                transaction.setFromAccount(account);
                condOneSatisfied = true;
            }

            if (entity.toId == account.getAccountId() && !condTwoSatisfied) {
                transaction.setToAccount(account);
                condTwoSatisfied = true;
            }
        }
    }

    /* Load accounts from database */
    private static void loadAccounts() {
        try {
            List<AccountEntity> accountList = new AsyncTask<Void, Void, List<AccountEntity>>() {
                @Override
                protected List<AccountEntity> doInBackground(Void... voids) {
                    List<AccountEntity> accounts = AppDatabase.getInstance(appContext).accountDao().getAll();
                    return accounts;
                }
            }.execute().get();

            // map account owner
            for (final AccountEntity entity : accountList) {
                Account account = AccountTransformer.transform(entity);

                mapAccountOwner(entity, account);

                accounts.add(account);
            }
        } catch (ExecutionException | InterruptedException e) {
        }
    }

    /* Load transactions from database */
    private static void loadTransactions() {
        try {
            List<TransactionEntity> transactionList = new AsyncTask<Void, Void, List<TransactionEntity>>() {
                @Override
                protected List<TransactionEntity> doInBackground(Void... voids) {
                    List<TransactionEntity> transactions = AppDatabase.getInstance(appContext).transactionDao().getAll();
                    return transactions;
                }
            }.execute().get();

            for (final TransactionEntity entity : transactionList) {
                Transaction transaction = TransactionTransformer.transform(entity);

                mapAccountsForTransaction(entity, transaction);

                transactions.add(transaction);
            }
        } catch (ExecutionException | InterruptedException e) {
        }
    }

    /* Load bank cards from database */
    private static void loadBankCards() {
        try {
            List<BankCardEntity> bankCardList = new AsyncTask<Void, Void, List<BankCardEntity>>() {
                @Override
                protected List<BankCardEntity> doInBackground(Void... voids) {
                    List<BankCardEntity> bankCards = AppDatabase.getInstance(appContext).bankCardDao().getAll();
                    return bankCards;
                }
            }.execute().get();

            for (final BankCardEntity entity : bankCardList) {
                BankCard bankCard = BankCardTransformer.transform(entity);

                mapAccountForBankCard(entity, bankCard);

                bankCards.add(bankCard);
            }
        } catch (ExecutionException | InterruptedException e) {
        }
    }
}