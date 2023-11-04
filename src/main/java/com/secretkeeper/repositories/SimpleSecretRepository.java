package com.secretkeeper.repositories;

import com.secretkeeper.entities.SimpleSecret;
import com.secretkeeper.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SimpleSecretRepository extends JpaRepository<SimpleSecret, Long> {
    SimpleSecret findBySecretId(Long secretId);
    SimpleSecret findByUserAndSecretId(User user, Long secretId);
    List<SimpleSecret> findByUser(User user);
}
