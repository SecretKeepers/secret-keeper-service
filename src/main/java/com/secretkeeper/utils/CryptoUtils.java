package com.secretkeeper.utils;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import static com.secretkeeper.constants.CryptoConstants.AES;
import static com.secretkeeper.constants.CryptoConstants.SECRET_KEY_ALGORITHM;

public class CryptoUtils {
    public static byte[] generateRandomBytes(int size) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[size];
        secureRandom.nextBytes(iv);
        return iv;
    }

    public static SecretKey secretKeyFromPassword(String password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM.getStrValue());
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
        SecretKey tmp = factory.generateSecret(keySpec);
        return new SecretKeySpec(tmp.getEncoded(), AES.getStrValue());
    }
}
