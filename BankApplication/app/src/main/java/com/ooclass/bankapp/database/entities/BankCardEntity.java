package com.ooclass.bankapp.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.ooclass.bankapp.models.Country;

import java.util.List;

@Entity(
        foreignKeys = {
                @ForeignKey(entity = AccountEntity.class,
                        parentColumns = "accountId",
                        childColumns = "linkedAccountId")},
        tableName = "bank_cards")
public class BankCardEntity {
    @PrimaryKey(autoGenerate = true)
    public long bankCardId;

    public long linkedAccountId;
    public String name;
    public float withdrawLimit;
    public float payLimit;
    public List<Country> allowedCountries;
}
