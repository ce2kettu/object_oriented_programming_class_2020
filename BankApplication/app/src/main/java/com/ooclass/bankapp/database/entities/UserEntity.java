package com.ooclass.bankapp.database.entities;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.ooclass.bankapp.models.Address;

@Entity(tableName = "users", indices = {@Index(value = {"emailAddress"},
        unique = true)})
public class UserEntity {
    @PrimaryKey(autoGenerate = true)
    public long userId;

    public String emailAddress; // unique
    public String password;
    public String salt;
    public String firstName;
    public String lastName;
    public String phoneNumber;

    @Embedded
    public Address address;
}