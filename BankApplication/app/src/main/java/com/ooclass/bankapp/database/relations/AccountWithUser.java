package com.ooclass.bankapp.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.ooclass.bankapp.database.entities.AccountEntity;
import com.ooclass.bankapp.database.entities.UserEntity;

public class AccountWithUser {
    @Embedded
    public AccountEntity account;

    @Relation(
            parentColumn = "ownerId",
            entityColumn = "userId",
            entity = UserEntity.class
    )
    public UserEntity user;
}