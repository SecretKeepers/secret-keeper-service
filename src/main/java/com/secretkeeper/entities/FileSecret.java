package com.secretkeeper.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper=true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FileSecret extends Secret{
    private String fileType;
    private String fileName;
    private Long fileSize;
    @Lob
    @Column(name = "data", columnDefinition="LONGBLOB")
    private byte[] data;

    @JsonBackReference(value = "user_id")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
