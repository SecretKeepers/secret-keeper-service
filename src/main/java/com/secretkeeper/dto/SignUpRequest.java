package com.secretkeeper.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
}
