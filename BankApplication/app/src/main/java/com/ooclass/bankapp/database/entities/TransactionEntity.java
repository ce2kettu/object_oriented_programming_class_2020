package com.ooclass.bankapp.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ooclass.bankapp.models.TransactionType;

import java.util.Date;

@Entity(tableName = "transactions")
public class TransactionEntity {
    @PrimaryKey(autoGenerate = true)
    public long transactionId;

    public TransactionType type;
    public String name;
    public long fromId;
    public long toId;
    public float amount;
    public Date date;
}
