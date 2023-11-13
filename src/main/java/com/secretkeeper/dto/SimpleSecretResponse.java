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
    private String id;
    private String type;
    private String secret;
    private String description;
    private Date createdAt;
}
