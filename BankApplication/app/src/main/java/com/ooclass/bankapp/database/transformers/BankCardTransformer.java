package com.ooclass.bankapp.database.transformers;

import com.ooclass.bankapp.database.entities.BankCardEntity;
import com.ooclass.bankapp.models.BankCard;

import java.util.ArrayList;
import java.util.List;

public final class BankCardTransformer {
    public static BankCard transform(BankCardEntity entity) {
        BankCard bankCard = null;
        if (entity != null) {
            bankCard = new BankCard();
            bankCard.setBankCardId(entity.bankCardId);
            bankCard.setName(entity.name);
            bankCard.setWithdrawalLimit(entity.withdrawLimit);
            bankCard.setPayLimit(entity.payLimit);
            bankCard.setAllowedCountries(entity.allowedCountries);
        }
        return bankCard;
    }

    public static BankCardEntity transform(BankCard bankCard, boolean isNew) {
        BankCardEntity entity = null;
        if (bankCard != null) {
            entity = new BankCardEntity();

            if (!isNew) {
                entity.bankCardId = bankCard.getBankCardId();
            }

            entity.name = bankCard.getName();
            entity.withdrawLimit = bankCard.getWithdrawalLimit();
            entity.payLimit = bankCard.getPayLimit();
            entity.allowedCountries = bankCard.getAllowedCountries();
            entity.linkedAccountId = bankCard.getLinkedAccount().getAccountId();
        }
        return entity;
    }

    public static List<BankCardEntity> transformListToEntities(List<BankCard> list) {
        final List<BankCardEntity> bankCardList = new ArrayList<BankCardEntity>();
        for (BankCard bankCard : list) {
            final BankCardEntity bankCardEntity = transform(bankCard, false);
            if (bankCardEntity != null) {
                bankCardList.add(bankCardEntity);
            }
        }
        return bankCardList;
    }
}
