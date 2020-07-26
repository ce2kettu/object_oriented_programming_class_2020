package com.ooclass.bankapp.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ooclass.bankapp.database.entities.TransactionEntity;
import com.ooclass.bankapp.database.relations.TransactionWithAccounts;

import java.util.List;

@Dao
public interface TransactionDao {
    @Query("SELECT * FROM transactions")
    List<TransactionEntity> getAll();

    @Transaction
    @Query("SELECT * FROM transactions")
    public List<TransactionWithAccounts> getAllFormatted();

    @Insert
    long insert(TransactionEntity transactionEntity);
}