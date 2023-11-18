package com.secretkeeper.constants;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CryptoConstantsTest {

    @Test
    public void testCipherAlgorithm() {
        assertThat(CryptoConstants.CIPHER_ALGORITHM.getStrValue()).isEqualTo("AES/GCM/NoPadding");
    }

    @Test
    public void testIVSizeBytes() {
        assertThat(CryptoConstants.IV_SIZE_BYTES.getIntValue()).isEqualTo(12);
    }

    @Test
    public void testSaltSizeBytes() {
        assertThat(CryptoConstants.SALT_SIZE_BYTES.getIntValue()).isEqualTo(16);
    }

    @Test
    public void testTagSizeBits() {
        assertThat(CryptoConstants.TAG_SIZE_BITS.getIntValue()).isEqualTo(128);
    }

    @Test
    public void testSecretKeyAlgorithm() {
        assertThat(CryptoConstants.SECRET_KEY_ALGORITHM.getStrValue()).isEqualTo("PBKDF2WithHmacSHA256");
    }

    @Test
    public void testAES() {
        assertThat(CryptoConstants.AES.getStrValue()).isEqualTo("AES");
    }
}
