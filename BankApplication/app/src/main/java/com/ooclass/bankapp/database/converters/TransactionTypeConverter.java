package com.ooclass.bankapp.database.converters;

import androidx.room.TypeConverter;

import com.ooclass.bankapp.models.TransactionType;

public class TransactionTypeConverter {
    @TypeConverter
    public static TransactionType fromInt(int value) {
        for (TransactionType type : TransactionType.values()) {
            if (type.getCode() == value) {
                return type;
            }
        }
        return null;
    }

    @TypeConverter
    public static int fromTransactionType(TransactionType type) {
        return type.getCode();
    }
}
