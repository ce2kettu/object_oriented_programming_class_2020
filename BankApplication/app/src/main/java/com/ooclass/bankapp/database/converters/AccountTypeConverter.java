package com.ooclass.bankapp.database.converters;

import androidx.room.TypeConverter;

import com.ooclass.bankapp.models.AccountType;

public class AccountTypeConverter {
    @TypeConverter
    public static AccountType fromInt(int value) {
        for (AccountType type : AccountType.values()) {
            if (type.getCode() == value) {
                return type;
            }
        }
        return null;
    }

    @TypeConverter
    public static int fromAccountType(AccountType type) {
        return type.getCode();
    }
}
