package com.secretkeeper.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Responses {
    INVALID_SECRET_TYPE("Invalid secret type!"),
    INVALID_USER_PASSWD("Invalid email or password!"),
    INVALID_MASTER_KEY("Invalid master key!");

    private final String msg;
}
