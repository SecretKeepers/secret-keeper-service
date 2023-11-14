package com.secretkeeper.services;

import com.secretkeeper.dto.SimpleSecretCreateRequest;
import com.secretkeeper.dto.SimpleSecretResponse;
import com.secretkeeper.entities.SimpleSecret;
import com.secretkeeper.entities.User;
import com.secretkeeper.repositories.SimpleSecretRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SimpleSecretService {
    private final SimpleSecretRepository simpleSecretRepository;
    private final CryptoService cryptoService;
    private final UserService userService;

    public SimpleSecret saveSecret(SimpleSecretCreateRequest request, String masterKey) {
        String encryptedSecret = cryptoService.encryptText(request.getSecret(), masterKey);
        SimpleSecret secret = SimpleSecret
                .builder()
                .secretType(request.getType())
                .secretValue(encryptedSecret)
                .secretDescription(request.getDescription())
                .user(userService.getAuthUserFromToken())
                .build();
        return simpleSecretRepository.save(secret);
    }

    public SimpleSecretResponse getSecret(String secretId, String masterKey){
        User user = userService.getAuthUserFromToken();
        //TODO
        //if secret is found then verify secret type matches the one in request else throw error
        //if secret not found then throw error: secret id or type invalid
        SimpleSecret secret = simpleSecretRepository.findByUserAndSecretId(user, secretId);
        String decryptedSecret = cryptoService.decryptText(secret.getSecretValue(), masterKey);
        return SimpleSecretResponse
                .builder()
                .id(secret.getSecretId())
                .type(secret.getSecretType())
                .secret(decryptedSecret)
                .description(secret.getSecretDescription())
                .createdAt(secret.getCreatedAt())
                .build();
    }

    public List<SimpleSecret> getAllSecretsEncrypted() {
        return simpleSecretRepository.findByUser(userService.getAuthUserFromToken());
    }

    public List<SimpleSecret> getAllSecretsDecrypted(String masterKey) {
        User user = userService.getAuthUserFromToken();
        List<SimpleSecret> secrets = simpleSecretRepository.findByUser(user);
        for(SimpleSecret secret: secrets) {
            String decipher = cryptoService.decryptText(secret.getSecretValue(), masterKey);
            secret.setSecretValue(decipher);
        }
        return secrets;
    }

}
