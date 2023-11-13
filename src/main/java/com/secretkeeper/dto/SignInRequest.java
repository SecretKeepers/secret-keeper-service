package com.secretkeeper.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {
    private String username;
    private String password;
}

