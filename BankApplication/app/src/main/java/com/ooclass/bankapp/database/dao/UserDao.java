package com.ooclass.bankapp.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ooclass.bankapp.database.entities.AccountEntity;
import com.ooclass.bankapp.database.entities.BankCardEntity;
import com.ooclass.bankapp.database.entities.UserEntity;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users")
    List<UserEntity> getAll();

    @Query("SELECT * FROM users WHERE userId IN (:userIds)")
    List<UserEntity> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM users WHERE firstName LIKE :first AND " +
            "lastName LIKE :last LIMIT 1")
    UserEntity findByName(String first, String last);

    @Query("SELECT * FROM users WHERE emailAddress = :email")
    UserEntity findByEmail(String email);

    @Insert(onConflict = IGNORE)
    long insert(UserEntity userEntity);

    @Update
    void update(UserEntity userEntity);

    @Delete
    void delete(UserEntity userEntity);

    @Query("SELECT * FROM accounts " +
            "INNER JOIN users ON users.userId = accounts.ownerId " +
            "WHERE ownerId = :userId")
    List<AccountEntity> loadAllAccounts(long userId);

    @Query("SELECT * FROM bank_cards " +
            "INNER JOIN accounts ON accounts.accountId = bank_cards.linkedAccountId " +
            "INNER JOIN users ON users.userId = accounts.ownerId " +
            "WHERE users.userId = :userId")
    List<BankCardEntity> loadAllBankCards(long userId);
}