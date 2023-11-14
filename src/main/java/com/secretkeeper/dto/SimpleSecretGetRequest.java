package com.secretkeeper.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleSecretGetRequest {
    private String type;
    private String secretId;
}
