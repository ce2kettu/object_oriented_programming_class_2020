package com.ooclass.bankapp.models;

import android.os.AsyncTask;
import android.util.Base64;

import com.ooclass.bankapp.application.BankApplication;
import com.ooclass.bankapp.database.AppDatabase;
import com.ooclass.bankapp.database.entities.UserEntity;
import com.ooclass.bankapp.database.transformers.UserTransformer;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public final class Auth {
    private static Auth instance;

    /* Generates a salt for password hashing */
    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        return bytes;
    }

    public static Auth getInstance() {
        if (instance == null) {
            instance = new Auth();
        }
        return instance;
    }

    /* Encodes the password with the provided salt using SHA-512 */
    private static String encodePassword(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);
        byte[] passwordData = md.digest(password.getBytes(StandardCharsets.UTF_8));

        StringBuilder hashedPassword = new StringBuilder();
        for (int i = 0; i < passwordData.length; i++) {
            hashedPassword.append(Integer.toString((passwordData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return hashedPassword.toString();
    }

    /* Update user instance password, but not in database */
    public static boolean updatePassword(User user, String password) {
        try {
            byte[] salt = generateSalt();
            String hashedPassword = encodePassword(password, salt);
            user.setPassword(hashedPassword);
            user.setSalt(Base64.encodeToString(salt, Base64.DEFAULT));

            return true;
        }  catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    /* Update user profile in database */
    public static void updateProfile(final User user) {
        new AsyncTask<Void, Void, Void>() {
           @Override
           protected Void doInBackground(Void... voids) {
               AppDatabase.getInstance(BankApplication.getAppContext()).userDao().update(UserTransformer.transform(user, false));
               return null;
           }
       }.execute();
    }

    /* Compares password to saved password */
    public static boolean comparePassword(String password, String hashedPassword, String salt) {
        try {
            return hashedPassword.equals(encodePassword(password, Base64.decode(salt, Base64.DEFAULT)));
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    /* Returns true if login is successful */
    public static boolean login(final String emailAddress, String password) {
        try {
            if (!isPasswordValid(password)) {
                return false;
            }

            UserEntity entity = new AsyncTask<Void, Void, UserEntity>() {
                @Override
                protected UserEntity doInBackground(Void... voids) {
                    UserEntity user = AppDatabase.getInstance(BankApplication.getAppContext()).userDao().findByEmail(emailAddress);
                    return user;
                }
            }.execute().get();

            if (entity == null)
                return false;

            return comparePassword(password, entity.password, entity.salt);
        } catch (ExecutionException | InterruptedException e) {
            return false;
        }
    }

    /* Returns true if registration is successful */
    public static boolean register(String emailAddress, String password, String firstName, String lastName) {
        try {
            if (!isPasswordValid(password)) {
                return false;
            }

            // Hash password before saving
            byte[] salt = generateSalt();
            String hashedPassword = encodePassword(password, salt);

            final User user = new User();

            // Should validate these but don't need to
            user.setEmailAddress(emailAddress);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPassword(hashedPassword);
            user.setSalt(Base64.encodeToString(salt, Base64.DEFAULT));

            // Add user in local cache
            BankApplication.getInstance().users.add(user);

            // Insert in database
            Long result = new AsyncTask<Void, Void, Long>() {
                @Override
                protected Long doInBackground(Void... voids) {
                    // userId is the id of the inserted row
                    long userId = AppDatabase.getInstance(BankApplication.getAppContext()).userDao().insert(UserTransformer.transform(user, true));
                    return userId;
                }
            }.execute().get();

            // Successful result
            if (result >= 0) {
                user.setUserId(result);
                // Cache user
                BankApplication.users.add(user);
                return true;
            } else {
                return false;
            }
        } catch (NoSuchAlgorithmException | ExecutionException | InterruptedException e) {
            return false;
        }
    }

    /* Cleans up application state to handle logout */
    public static void logout() {
        BankApplication.getInstance().setCurrentUser(null);
    }

    /* Returns true if the string has at least 12 characters, at least one digit,
       one lowercase character, one uppercase character, and one special character. */
    public static boolean isPasswordValid(String password) {
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\\$%\\^&\\*\"\\+\\-\\*\\,\\.\\?\\=\\/\\(\\)\\\\]).{12,}$");
    }

    public static String generate2FACode() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }
}
