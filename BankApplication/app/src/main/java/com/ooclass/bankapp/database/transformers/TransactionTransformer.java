package com.ooclass.bankapp.database.transformers;

import com.ooclass.bankapp.database.entities.TransactionEntity;
import com.ooclass.bankapp.models.Transaction;

import java.util.ArrayList;
import java.util.List;

public final class TransactionTransformer {
    public static Transaction transform(TransactionEntity entity) {
        Transaction transaction = null;
        if (entity != null) {
            transaction = new Transaction();
            transaction.setTransactionId(entity.transactionId);
            transaction.setType(entity.type);
            transaction.setName(entity.name);
            transaction.setAmount(entity.amount);
            transaction.setDate(entity.date);
        }
        return transaction;
    }

    public static TransactionEntity transform(Transaction transaction, boolean isNew) {
        TransactionEntity entity  = null;
        if (transaction != null) {
            entity = new TransactionEntity();

            if (!isNew) {
                entity.transactionId = transaction.getTransactionId();
            }

            entity.type = transaction.getType();
            entity.name = transaction.getName();
            entity.amount = transaction.getAmount();
            entity.date = transaction.getDate();

            if (transaction.getFromAccount() != null) {
                entity.fromId = transaction.getFromAccount().getAccountId();
            }

            if (transaction.getToAccount() != null) {
                entity.toId = transaction.getToAccount().getAccountId();
            }

        }
        return entity;
    }

    public static List<TransactionEntity> transformListToEntities(List<Transaction> list) {
        final List<TransactionEntity> transactionList = new ArrayList<TransactionEntity>();
        for (Transaction transaction : list) {
            final TransactionEntity transactionEntity = transform(transaction, false);
            if (transactionEntity != null) {
                transactionList.add(transactionEntity);
            }
        }
        return transactionList;
    }
}
