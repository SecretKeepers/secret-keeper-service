package com.secretkeeper.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SetMasterKeyRequest {
    private String masterKey;
}
