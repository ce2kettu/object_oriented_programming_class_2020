package com.ooclass.bankapp.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.ooclass.bankapp.database.entities.AccountEntity;
import com.ooclass.bankapp.database.entities.TransactionEntity;

public class TransactionWithAccounts {
    @Embedded
    public TransactionEntity transaction;

    @Relation(
            parentColumn = "fromId",
            entityColumn = "accountId",
            entity = AccountEntity.class
    )
    public AccountWithUser fromAccount;

    @Relation(
            parentColumn = "toId",
            entityColumn = "accountId",
            entity = AccountEntity.class
    )
    public AccountWithUser toAccount;
}
