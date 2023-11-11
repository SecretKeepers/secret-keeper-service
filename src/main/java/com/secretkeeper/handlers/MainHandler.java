package com.secretkeeper.handlers;

import com.secretkeeper.constants.Responses;
import com.secretkeeper.constants.SecretTypes;
import com.secretkeeper.dto.SimpleSecretCreateRequest;
import com.secretkeeper.dto.SimpleSecretGetRequest;
import com.secretkeeper.dto.SimpleSecretResponse;
import com.secretkeeper.entities.SimpleSecret;
import com.secretkeeper.services.JwtService;
import com.secretkeeper.services.SimpleSecretService;
import com.secretkeeper.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RestController
@RequestMapping("/secret")
public class MainHandler {

    private final SimpleSecretService simpleSecretService;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<?> simpleSecretAdd(@RequestBody SimpleSecretCreateRequest secretRequest, HttpServletRequest request){
        String token = jwtService.getJwtFromRequest(request);
        String masterKey = jwtService.extractClaim(token, "masterKey");
        if(!userService.isMasterHashValid(masterKey)){
            return ResponseEntity.badRequest().body(Responses.INVALID_MASTER_KEY.getMsg());
        }
        if(secretRequest.getType().equalsIgnoreCase(SecretTypes.SIMPLE.getType())) {
            SimpleSecret savedSecret = simpleSecretService.saveSecret(secretRequest, masterKey);
            return new ResponseEntity<>(savedSecret, HttpStatus.CREATED);
        }
        else return ResponseEntity.badRequest().body(Responses.INVALID_SECRET_TYPE.getMsg());
    }

    @PostMapping("/get")
    public ResponseEntity<?> getSimpleSecret(@RequestBody SimpleSecretGetRequest secretRequest, HttpServletRequest request){
        String token = jwtService.getJwtFromRequest(request);
        String masterKey = jwtService.extractClaim(token, "masterKey");
        if(!userService.isMasterHashValid(masterKey)){
            return ResponseEntity.badRequest().body(Responses.INVALID_MASTER_KEY.getMsg());
        }
        if(secretRequest.getType().equalsIgnoreCase(SecretTypes.SIMPLE.getType())) {
            SimpleSecretResponse secret = simpleSecretService.getSecret(secretRequest.getSecretId(), masterKey);
            return ResponseEntity.ok(secret);
        }
        return ResponseEntity.badRequest().body(Responses.INVALID_SECRET_TYPE.getMsg());
    }

    @GetMapping("/get/all/encrypted")
    public ResponseEntity<?> getAllSecretsEncrypted(@RequestParam String type) {
        if(type.equalsIgnoreCase(SecretTypes.SIMPLE.getType())) {
            return ResponseEntity.ok(simpleSecretService.getAllSecrets());
        }
        return ResponseEntity.badRequest().body(Responses.INVALID_SECRET_TYPE.getMsg());
    }

    @GetMapping("/get/all/decrypted")
    public ResponseEntity<?> getAllSecretsDecrypted(@RequestParam String type, HttpServletRequest request) {
        String token = jwtService.getJwtFromRequest(request);
        String masterKey = jwtService.extractClaim(token, "masterKey");
        if(!userService.isMasterHashValid(masterKey)){
            return ResponseEntity.badRequest().body(Responses.INVALID_MASTER_KEY.getMsg());
        }
        if(type.equalsIgnoreCase(SecretTypes.SIMPLE.getType())) {
            return ResponseEntity.ok(simpleSecretService.getAllSecretsDecrypted(masterKey));
        }
        return ResponseEntity.badRequest().body(Responses.INVALID_SECRET_TYPE.getMsg());
    }

    @GetMapping("/users")
    public String usersEndPoint() {
        return "ONLY users can see this";
    }
}
