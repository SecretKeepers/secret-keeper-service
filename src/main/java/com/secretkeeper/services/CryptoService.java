package com.secretkeeper.services;

import com.secretkeeper.configs.CryptoConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CryptoService {
    @Autowired
    private final CryptoConfig cryptoConfig;
    private static final String AES = "AES";
    private static final String AES_CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int IV_SIZE = 16;

    public String encrypt(String value, String password) {
        Objects.requireNonNull(value, "Value cannot be null!");
        Objects.requireNonNull(password, "Password cannot be null!");
        try {
            byte[] iv = generateIV(); // Generate a random IV
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            // Create a key using password-based key derivation
            SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), cryptoConfig.getSalt(), 65536, 256);
            SecretKey tmp = factory.generateSecret(keySpec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), AES);

            // Encrypt the data using AES-CBC
            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, secret, ivParameterSpec);
            byte[] encryptedBytes = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            byte[] encBytesWithIV = new byte[iv.length + encryptedBytes.length];
            System.arraycopy(iv, 0, encBytesWithIV, 0, iv.length);
            System.arraycopy(encryptedBytes, 0, encBytesWithIV, iv.length, encryptedBytes.length);
            return Base64.getEncoder().encodeToString(encBytesWithIV);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                 IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException |
                 InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    public String decrypt(String encryptedValue, String password) {
        Objects.requireNonNull(encryptedValue, "Encrypted value cannot be null!");
        Objects.requireNonNull(password, "Password cannot be null!");
        try {
            byte[] encBytesWithIV = Base64.getDecoder().decode(encryptedValue);

            // Extract IV from cipher + iv
            byte[] iv = new byte[IV_SIZE];
            System.arraycopy(encBytesWithIV, 0, iv, 0, iv.length);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            // Extract cipher text from cipher + iv
            byte[] ciphertext = new byte[encBytesWithIV.length - iv.length];
            System.arraycopy(encBytesWithIV, iv.length, ciphertext, 0, ciphertext.length);

            // Create a key using password-based key derivation
            SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), cryptoConfig.getSalt(), 65536, 256);
            SecretKey tmp = factory.generateSecret(keySpec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), AES);

            // Decrypt the data using AES-CBC
            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, secret, ivParameterSpec);
            byte[] decryptedBytes = cipher.doFinal(ciphertext);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                 IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException |
                 InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    private byte[] generateIV() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[IV_SIZE];
        secureRandom.nextBytes(iv);
        return iv;
    }
}
