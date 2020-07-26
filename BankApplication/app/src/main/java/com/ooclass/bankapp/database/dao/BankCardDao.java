package com.ooclass.bankapp.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.ooclass.bankapp.database.entities.BankCardEntity;
import com.ooclass.bankapp.database.relations.BankCardWithAccount;

import java.util.List;

@Dao
public interface BankCardDao {
    @Query("SELECT * FROM bank_cards")
    List<BankCardEntity> getAll();

    @Transaction
    @Query("SELECT * FROM bank_cards")
    public List<BankCardWithAccount> getAllFormatted();

    @Insert
    long insert(BankCardEntity entity);

    @Update
    void update(BankCardEntity entity);
}