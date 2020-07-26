package com.ooclass.bankapp.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.ooclass.bankapp.database.entities.AccountEntity;
import com.ooclass.bankapp.database.entities.TransactionEntity;
import com.ooclass.bankapp.database.relations.AccountWithUser;

import java.util.List;

@Dao
public interface AccountDao {
    @Query("SELECT * FROM accounts")
    List<AccountEntity> getAll();

    @Transaction
    @Query("SELECT * FROM accounts")
    public List<AccountWithUser> getAllFormatted();

    @Query("SELECT * FROM accounts " +
            "INNER JOIN transactions ON transactions.fromId = accounts.accountId OR transactions.toId = accounts.accountId " +
            "WHERE accountId = :accountId")
    List<TransactionEntity> getAllTransactions(long accountId);

    @Insert
    long insert(AccountEntity accountEntity);

    @Update
    void update(AccountEntity entity);
}