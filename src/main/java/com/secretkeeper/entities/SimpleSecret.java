package com.secretkeeper.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper=true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class SimpleSecret extends Secret {

    private String secretValue;
    private String secretDescription;

    @JsonBackReference(value = "user_id")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
