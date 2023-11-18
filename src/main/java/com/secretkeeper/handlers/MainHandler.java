package com.secretkeeper.handlers;

import com.secretkeeper.constants.Responses;
import com.secretkeeper.constants.SecretTypes;
import com.secretkeeper.dto.SimpleSecretCreateRequest;
import com.secretkeeper.dto.SimpleSecretGetRequest;
import com.secretkeeper.dto.SimpleSecretResponse;
import com.secretkeeper.entities.FileSecret;
import com.secretkeeper.entities.SimpleSecret;
import com.secretkeeper.services.FileSecretService;
import com.secretkeeper.services.JwtService;
import com.secretkeeper.services.SimpleSecretService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
//TODO add exceptions for masterKey not present

@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RestController
@RequestMapping("/secret")
public class MainHandler {

    private final SimpleSecretService simpleSecretService;
    private final FileSecretService fileSecretService;
    private final JwtService jwtService;

    @PostMapping("/create")
    //TODO validate content type and call respective service
    public ResponseEntity<?> simpleSecretAdd(@RequestBody SimpleSecretCreateRequest secretRequest){
        String masterKey = jwtService.getMasterKeyFromJWT();
        if(secretRequest.getType().equalsIgnoreCase(SecretTypes.SIMPLE.getType())) {
            SimpleSecret savedSecret = simpleSecretService.saveSecret(secretRequest, masterKey);
            return new ResponseEntity<>(savedSecret, HttpStatus.CREATED);
        }
        else return ResponseEntity.badRequest().body(Responses.INVALID_SECRET_TYPE.getMsg());
    }

    @PostMapping("/get")
    public ResponseEntity<?> getSimpleSecret(@RequestBody SimpleSecretGetRequest secretRequest){
        String masterKey = jwtService.getMasterKeyFromJWT();
        if(secretRequest.getType().equalsIgnoreCase(SecretTypes.SIMPLE.getType())) {
            SimpleSecretResponse secret = simpleSecretService.getSecret(secretRequest.getSecretId(), masterKey);
            return ResponseEntity.ok(secret);
        }
        return ResponseEntity.badRequest().body(Responses.INVALID_SECRET_TYPE.getMsg());
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllSecretsEncrypted(@RequestParam String type) {
        if(type.equalsIgnoreCase(SecretTypes.SIMPLE.getType())) {
            return ResponseEntity.ok(simpleSecretService.getAllSecretsEncrypted());
        }
        else if(type.equalsIgnoreCase(SecretTypes.FILE.getType())) {
            return ResponseEntity.ok(fileSecretService.getAll());
        }
        return ResponseEntity.badRequest().body(Responses.INVALID_SECRET_TYPE.getMsg());
    }

    @GetMapping("/all/decrypted")
    public ResponseEntity<?> getAllSecretsDecrypted(@RequestParam String type) {
        String masterKey = jwtService.getMasterKeyFromJWT();
        if(type.equalsIgnoreCase(SecretTypes.SIMPLE.getType())) {
            return ResponseEntity.ok(simpleSecretService.getAllSecretsDecrypted(masterKey));
        }
        return ResponseEntity.badRequest().body(Responses.INVALID_SECRET_TYPE.getMsg());
    }

    @PostMapping("/docs/upload")
    public ResponseEntity<?> uploadSecretFile(@RequestParam("file") MultipartFile file, @RequestParam String description) {
        try {
            String masterKey = jwtService.getMasterKeyFromJWT();
            FileSecret secret = fileSecretService.save(file, description, masterKey);
            return ResponseEntity.ok("File saved as secret: " + secret.getSecretId());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error");
        }
    }

    @GetMapping("/docs/download")
    public ResponseEntity<?> downloadSecretFile(@RequestParam("id") String id) {
        try {
            String masterKey = jwtService.getMasterKeyFromJWT();
            return fileSecretService.get(id, masterKey);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/users")
    public String usersEndPoint() {
        return "ONLY users can see this";
    }
}
