package com.secretkeeper.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleSecretCreateRequest {
    private String type;
    private String secret;
    private String description;
}
