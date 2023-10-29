package com.secretkeeper.repositories;

import com.secretkeeper.entities.SimpleSecret;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SimpleSecretRepository extends JpaRepository<SimpleSecret, Long> {
    SimpleSecret findBySecretId(Long secretId);
}
