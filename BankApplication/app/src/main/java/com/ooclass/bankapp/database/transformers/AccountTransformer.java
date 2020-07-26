package com.ooclass.bankapp.database.transformers;

import com.ooclass.bankapp.database.entities.AccountEntity;
import com.ooclass.bankapp.models.Account;

import java.util.ArrayList;
import java.util.List;

public final class AccountTransformer {
    public static Account transform(AccountEntity entity) {
        Account account = null;
        if (entity != null) {
            account = new Account();
            account.setAccountId(entity.accountId);
            account.setType(entity.type);
            account.setName(entity.name);
            account.setBalance(entity.balance);
            account.setPayable(entity.isPayable);
            account.setTransferable(entity.isTransferable);
        }
        return account;
    }

    public static AccountEntity transform(Account account, boolean isNew) {
        AccountEntity entity = null;
        if (account != null) {
            entity = new AccountEntity();

            if (!isNew) {
                entity.accountId = account.getAccountId();
            }

            entity.type = account.getType();
            entity.ownerId = account.getOwner().getUserId();
            entity.name = account.getName();
            entity.balance = account.getBalance();
            entity.isPayable = account.isPayable();
            entity.isTransferable = account.isTransferable();
        }
        return entity;
    }

    public static List<AccountEntity> transformListToEntities(List<Account> list) {
        final List<AccountEntity> accountList = new ArrayList<AccountEntity>();
        for (Account account : list) {
            final AccountEntity accountEntity = transform(account, false);
            if (accountEntity != null) {
                accountList.add(accountEntity);
            }
        }
        return accountList;
    }
}
