package com.ooclass.bankapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ooclass.bankapp.database.converters.AccountTypeConverter;
import com.ooclass.bankapp.database.converters.CountryListConverter;
import com.ooclass.bankapp.database.converters.DateConverter;
import com.ooclass.bankapp.database.converters.TransactionTypeConverter;
import com.ooclass.bankapp.database.dao.AccountDao;
import com.ooclass.bankapp.database.dao.BankCardDao;
import com.ooclass.bankapp.database.dao.TransactionDao;
import com.ooclass.bankapp.database.dao.UserDao;
import com.ooclass.bankapp.database.entities.AccountEntity;
import com.ooclass.bankapp.database.entities.BankCardEntity;
import com.ooclass.bankapp.database.entities.TransactionEntity;
import com.ooclass.bankapp.database.entities.UserEntity;

@Database(entities = {UserEntity.class, TransactionEntity.class, AccountEntity.class, BankCardEntity.class}, version = 11, exportSchema = true)
@TypeConverters({DateConverter.class, CountryListConverter.class, TransactionTypeConverter.class, AccountTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "bank-app";
    private static volatile AppDatabase instance;

    public abstract UserDao userDao();
    public abstract AccountDao accountDao();
    public abstract TransactionDao transactionDao();
    public abstract BankCardDao bankCardDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DB_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return instance;
    }
}