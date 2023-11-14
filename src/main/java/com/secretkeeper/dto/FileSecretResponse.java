package com.secretkeeper.dto;

import com.secretkeeper.entities.FileSecret;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileSecretResponse {
    private Date createdAt;
    private String secretId;
    private String fileName;
    private Long fileSize;
    private String fileType;
    private String description;

    public FileSecretResponse(FileSecret file) {
        this.createdAt = file.getCreatedAt();
        this.secretId = file.getSecretId();
        this.fileName = file.getFileName();
        this.fileSize = file.getFileSize();
        this.fileType = file.getFileType();
        this.description = file.getSecretDescription();
    }

}
