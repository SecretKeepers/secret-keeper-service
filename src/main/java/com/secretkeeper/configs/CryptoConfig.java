package com.secretkeeper.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import static ch.qos.logback.core.encoder.ByteArrayUtil.hexStringToByteArray;

@Configuration
public class CryptoConfig {
    @Value("${crypto.salt}")
    private byte[] salt;

    @Value("${crypto.iv}")
    private String ivHex;


    public byte[] getSalt() {
        return salt;
    }

    public byte[] getIv() {
        return hexStringToByteArray(ivHex);
    }
}
