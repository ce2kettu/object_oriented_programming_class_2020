package com.ooclass.bankapp.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.ooclass.bankapp.models.AccountType;

@Entity(
        foreignKeys = {
                @ForeignKey(entity = UserEntity.class,
                        parentColumns = "userId",
                        childColumns = "ownerId")},
        tableName = "accounts")
public class AccountEntity {
    @PrimaryKey(autoGenerate = true)
    public long accountId;

    public AccountType type;
    public long ownerId;
    public String name;
    public float balance;
    public boolean isPayable;
    public boolean isTransferable;
}
