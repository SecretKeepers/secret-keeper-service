package com.secretkeeper.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SecretTypes {
    SIMPLE("simple"),
    PASSWORD("password"),
    FILE("file"),
    TOKEN("token");

    private final String type;
}
