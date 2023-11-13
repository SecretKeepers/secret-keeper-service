package com.secretkeeper.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleSecretGetRequest {
    private String type;
    private String secretId;
}
