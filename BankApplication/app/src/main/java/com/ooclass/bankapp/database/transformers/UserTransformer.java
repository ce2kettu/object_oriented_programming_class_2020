package com.ooclass.bankapp.database.transformers;

import com.ooclass.bankapp.database.entities.UserEntity;
import com.ooclass.bankapp.models.Address;
import com.ooclass.bankapp.models.User;

import java.util.ArrayList;
import java.util.List;

public final class UserTransformer {
    public static User transform(UserEntity userEntity) {
        User user = null;
        if (userEntity != null) {
            user = new User();
            user.setUserId(userEntity.userId);
            user.setEmailAddress(userEntity.emailAddress);
            user.setPassword(userEntity.password);
            user.setSalt(userEntity.salt);
            user.setFirstName(userEntity.firstName);
            user.setLastName(userEntity.lastName);
            user.setPhoneNumber(userEntity.phoneNumber);

            if (userEntity.address != null) {
                user.setAddress(userEntity.address);
            } else {
                user.setAddress(new Address());
            }
        }
        return user;
    }

    public static UserEntity transform(User user, boolean isNew) {
        UserEntity userEntity = null;
        if (user != null) {
            userEntity = new UserEntity();

            if (!isNew) {
                userEntity.userId = user.getUserId();
            }

            userEntity.emailAddress = user.getEmailAddress();
            userEntity.password = user.getPassword();
            userEntity.salt = user.getSalt();
            userEntity.firstName = user.getFirstName();
            userEntity.lastName = user.getLastName();
            userEntity.phoneNumber = user.getPhoneNumber();
            userEntity.address = user.getAddress();
        }
        return userEntity;
    }

    public static List<User> transform(List<UserEntity> userEntityCollection) {
        final List<User> userList = new ArrayList<User>();
        for (UserEntity userEntity : userEntityCollection) {
            final User user = transform(userEntity);
            if (user != null) {
                userList.add(user);
            }
        }
        return userList;
    }

    public static List<UserEntity> transformListToEntities(List<User> list) {
        final List<UserEntity> userList = new ArrayList<UserEntity>();
        for (User user : list) {
            final UserEntity userEntity = transform(user, false);
            if (userEntity != null) {
                userList.add(userEntity);
            }
        }
        return userList;
    }
}
