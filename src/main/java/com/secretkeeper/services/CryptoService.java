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

    public String encrypt(String value, String password) {
        Objects.requireNonNull(value, "Value cannot be null!");
        Objects.requireNonNull(password, "Password cannot be null!");
        try {
            // Create a key using password-based key derivation
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), cryptoConfig.getSalt(), 65536, 256); // You can adjust these parameters
            SecretKey tmp = factory.generateSecret(keySpec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), AES);

            // Encrypt the data using AES-CBC
            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(cryptoConfig.getIv()));
            byte[] encryptedBytes = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                 IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException |
                 InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    public String decrypt(String encryptedValue, String password) {
        Objects.requireNonNull(encryptedValue, "Encrypted value cannot be null");
        Objects.requireNonNull(password, "Password cannot be null");
        try {
            // Create a key using password-based key derivation
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), cryptoConfig.getSalt(), 65536, 256); // You can adjust these parameters
            SecretKey tmp = factory.generateSecret(keySpec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), AES);

            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedValue);

            // Decrypt the data using AES-CBC
            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(cryptoConfig.getIv()));
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                 IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException |
                 InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }
}
