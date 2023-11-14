package com.secretkeeper.services;

import com.secretkeeper.utils.CryptoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Objects;

import static com.secretkeeper.constants.CryptoConstants.CIPHER_ALGORITHM;
import static com.secretkeeper.constants.CryptoConstants.IV_SIZE_BYTES;
import static com.secretkeeper.constants.CryptoConstants.SALT_SIZE_BYTES;
import static com.secretkeeper.constants.CryptoConstants.TAG_SIZE_BITS;
import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@RequiredArgsConstructor
public class CryptoService {

    private byte[] encrypt(byte[] value, String password) {
        Objects.requireNonNull(value, "Value cannot be null!");
        Objects.requireNonNull(password, "Password cannot be null!");
        try {
            byte[] iv = CryptoUtils.generateRandomBytes(IV_SIZE_BYTES.getIntValue());
            byte[] salt = CryptoUtils.generateRandomBytes(SALT_SIZE_BYTES.getIntValue());
            // Create a key using password-based key derivation
            SecretKey secret = CryptoUtils.secretKeyFromPassword(password, salt);
            // Encrypt the data using AES-GCM
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM.getStrValue());
            cipher.init(Cipher.ENCRYPT_MODE, secret, new GCMParameterSpec(TAG_SIZE_BITS.getIntValue(), iv));
            byte[] encryptedBytes = cipher.doFinal(value);
            byte[] encBytesWithIVSalt = ByteBuffer.allocate(iv.length + salt.length + encryptedBytes.length)
                    .put(iv)
                    .put(salt)
                    .put(encryptedBytes)
                    .array();
            return Base64.getEncoder().encode(encBytesWithIVSalt);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                 IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException |
                 InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    private byte[] decrypt(byte[] encryptedValue, String password) {
        Objects.requireNonNull(encryptedValue, "Encrypted value cannot be null!");
        Objects.requireNonNull(password, "Password cannot be null!");
        try {
            byte[] encWithIVSalt = Base64.getDecoder().decode(encryptedValue);
            ByteBuffer encByteBuffer = ByteBuffer.wrap(encWithIVSalt);
            // Extract IV from cipher + iv + salt
            byte[] iv = new byte[IV_SIZE_BYTES.getIntValue()];
            encByteBuffer.get(iv);

            byte[] salt = new byte[SALT_SIZE_BYTES.getIntValue()];
            encByteBuffer.get(salt);

            byte[] cipherText = new byte[encByteBuffer.remaining()];
            encByteBuffer.get(cipherText);

            SecretKey secret = CryptoUtils.secretKeyFromPassword(password, salt);

            // Decrypt the data using AES-CBC
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM.getStrValue());
            cipher.init(Cipher.DECRYPT_MODE, secret, new GCMParameterSpec(TAG_SIZE_BITS.getIntValue(), iv));
            return cipher.doFinal(cipherText);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                 IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException |
                 InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    public String encryptText(final String text, final String password) {
        return new String(encrypt(text.getBytes(UTF_8), password));
    }

    public String decryptText(final String text, final String password) {
        return new String(decrypt(text.getBytes(UTF_8), password));
    }

    public byte[] encryptFile(byte[] data, String password) {
        return encrypt(data, password);
    }

    public byte[] decryptFile(byte[] data, String password) {
        return decrypt(data, password);
    }

}
