package com.secretkeeper.constants;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CryptoConstantsTests {

    @Test
    public void testCipherAlgorithm() {
        assertEquals("AES/GCM/NoPadding", CryptoConstants.CIPHER_ALGORITHM.getStrValue());
    }

    @Test
    public void testIVSizeBytes() {
        assertEquals(12, CryptoConstants.IV_SIZE_BYTES.getIntValue());
    }

    @Test
    public void testSaltSizeBytes() {
        assertEquals(16, CryptoConstants.SALT_SIZE_BYTES.getIntValue());
    }

    @Test
    public void testTagSizeBits() {
        assertEquals(128, CryptoConstants.TAG_SIZE_BITS.getIntValue());
    }

    @Test
    public void testSecretKeyAlgorithm() {
        assertEquals("PBKDF2WithHmacSHA256", CryptoConstants.SECRET_KEY_ALGORITHM.getStrValue());
    }

    @Test
    public void testAES() {
        assertEquals("AES", CryptoConstants.AES.getStrValue());
    }
}

