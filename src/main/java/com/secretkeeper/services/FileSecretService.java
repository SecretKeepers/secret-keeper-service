package com.secretkeeper.services;

import com.secretkeeper.constants.SecretTypes;
import com.secretkeeper.dto.FileSecretResponse;
import com.secretkeeper.entities.FileSecret;
import com.secretkeeper.repositories.FileSecretRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileSecretService {
    private final UserService userService;
    private final FileSecretRepository fileStore;
    private final CryptoService cryptoService;
    @Transactional
    public FileSecret save(MultipartFile file, String description, String masterKey) throws Exception {

        try {
            FileSecret secret = FileSecret
                    .builder()
                    .user(userService.getAuthUserFromToken())
                    .secretDescription(description)
                    .secretType(SecretTypes.FILE.getType())
                    .fileName(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .fileSize(file.getSize())
                    .build();
            try (InputStream inputStream = file.getInputStream()) {
                byte[] fileData = inputStream.readAllBytes();
                byte[] encFileData = cryptoService.encryptFile(fileData, masterKey);
                secret.setData(encFileData);
            }
            return fileStore.save(secret);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> get(String id, String masterKey) {
        try {
            FileSecret secret = fileStore.findByUserAndSecretId(userService.getAuthUserFromToken(), id);
            if (secret == null) {
                return ResponseEntity.notFound().build();
            }
            byte[] decryptedData = cryptoService.decryptFile(secret.getData(), masterKey);
            InputStream inputStream = new ByteArrayInputStream(decryptedData);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", secret.getFileName());
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error!");
        }
    }

    public List<FileSecretResponse> getAll() {
        List<FileSecret> secrets = fileStore.findByUser(userService.getAuthUserFromToken());
        List<FileSecretResponse> responseList = new ArrayList<>(secrets.size());
        secrets.forEach(fileSecret -> {
            responseList.add(new FileSecretResponse(fileSecret));
        });
        return responseList;
    }

}
