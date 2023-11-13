package com.secretkeeper.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CryptoConstants {
    CIPHER_ALGORITHM("AES/GCM/NoPadding"),
    IV_SIZE_BYTES(12),
    SALT_SIZE_BYTES(16),
    TAG_SIZE_BITS(128),
    SECRET_KEY_ALGORITHM("PBKDF2WithHmacSHA256"),
    AES("AES");

    private final Object value;

    public int getIntValue() {
        return (int) value;
    }

    public String getStrValue() {
        return (String) value;
    }
}
