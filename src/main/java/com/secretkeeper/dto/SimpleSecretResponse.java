package com.secretkeeper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleSecretResponse {
    String id;
    String type;
    String secret;
    String description;
    Date createdAt;
}
