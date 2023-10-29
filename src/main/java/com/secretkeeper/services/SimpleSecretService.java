package com.secretkeeper.services;

import com.secretkeeper.dto.SimpleSecretRequest;
import com.secretkeeper.dto.SimpleSecretResponse;
import com.secretkeeper.entities.SimpleSecret;
import com.secretkeeper.entities.User;
import com.secretkeeper.repositories.SimpleSecretRepository;
import com.secretkeeper.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimpleSecretService {
    private final SimpleSecretRepository simpleSecretRepository;
    private final UserRepository userRepository;

    public SimpleSecret saveSecret(SimpleSecretRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        SimpleSecret secret = SimpleSecret
                .builder()
                .type(request.getType())
                .secretValue(request.getSecret())
                .secretDescription(request.getDescription())
                .user(user)
                .build();
        SimpleSecret savedSecret = simpleSecretRepository.save(secret);
        return savedSecret;
//        return SimpleSecretResponse
//                .builder()
//                .id(String.valueOf(savedSecret.getSecretId()))
//                .type(savedSecret.getType())
//                .secret(savedSecret.getSecretValue())
//                .description(savedSecret.getSecretDescription())
//                .createdAt(savedSecret.getCreatedAt())
//                .build();
    }

}
