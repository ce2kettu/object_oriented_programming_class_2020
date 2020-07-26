package com.ooclass.bankapp.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.ooclass.bankapp.database.entities.AccountEntity;
import com.ooclass.bankapp.database.entities.BankCardEntity;

public class BankCardWithAccount {
    @Embedded
    public BankCardEntity bankCard;

    @Relation(
            parentColumn = "linkedAccountId",
            entityColumn = "accountId",
            entity = AccountEntity.class
    )
    public AccountWithUser linkedAccount;
}
