package com.secretkeeper.repositories;

import com.secretkeeper.entities.FileSecret;
import com.secretkeeper.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileSecretRepository extends JpaRepository<FileSecret, String> {
    List<FileSecret> findByUser(User user);
    FileSecret findByUserAndSecretId(User user, String secretId);
}
