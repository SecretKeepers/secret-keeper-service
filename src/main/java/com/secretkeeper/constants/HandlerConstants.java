package com.secretkeeper.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HandlerConstants {
    MASTER_KEY("masterKey");

    private final String value;
}
