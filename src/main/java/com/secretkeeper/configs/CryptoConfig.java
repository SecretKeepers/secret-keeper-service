package com.secretkeeper.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CryptoConfig {
    @Value("${crypto.salt}")
    private byte[] salt;

    public byte[] getSalt() {
        return salt;
    }

}
