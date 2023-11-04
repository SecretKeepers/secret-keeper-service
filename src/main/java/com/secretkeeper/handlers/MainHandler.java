package com.secretkeeper.handlers;

import com.secretkeeper.dto.SetMasterKeyRequest;
import com.secretkeeper.dto.SimpleSecretCreateRequest;
import com.secretkeeper.dto.SimpleSecretGetRequest;
import com.secretkeeper.dto.SimpleSecretResponse;
import com.secretkeeper.entities.SimpleSecret;
import com.secretkeeper.services.SimpleSecretService;
import com.secretkeeper.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RestController
@RequestMapping("/secret")
public class MainHandler {

    private final SimpleSecretService simpleSecretService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> simpleSecretAdd(@RequestBody SimpleSecretCreateRequest request){
        if(!userService.isMasterKeyValid(request.getMasterKey())){
            return ResponseEntity.badRequest().body("Master Key is invalid!");
        }
        if(request.getType().equals("Simple")) {
            SimpleSecret savedSecret = simpleSecretService.saveSecret(request);
            return new ResponseEntity<>(savedSecret, HttpStatus.CREATED);
        }
        else return ResponseEntity.badRequest().body("Invalid secret type!");
    }

    @PostMapping("/get")
    public ResponseEntity<?> getSimpleSecret(@RequestBody SimpleSecretGetRequest request){
        if(!userService.isMasterKeyValid(request.getMasterKey())){
            return ResponseEntity.badRequest().body("Master Key is invalid!");
        }
        if(request.getType().equals("Simple")) {
            SimpleSecretResponse secret = simpleSecretService.getSecret(request.getSecretId(), request.getMasterKey());
            return ResponseEntity.ok(secret);
        }
        return ResponseEntity.badRequest().body("Invalid secret type!");
    }

    @PostMapping("/master/set")
    public ResponseEntity<?> setMasterKey(@RequestBody SetMasterKeyRequest request){
        return userService.setMasterKey(request.getMasterKey());
    }

    @PostMapping("/master/validate")
    public ResponseEntity<?> validateMasterKey(@RequestBody Map<String, Object> request){
        String masterKey = (String) request.get("masterKey");
        if(userService.isMasterKeyValid(masterKey))
            return ResponseEntity.ok("Master Key is valid");
        else return ResponseEntity.badRequest().body("Master Key is invalid!");
    }

    @GetMapping("/users")
    public String usersEndPoint() {
        return "ONLY users can see this";
    }
}
