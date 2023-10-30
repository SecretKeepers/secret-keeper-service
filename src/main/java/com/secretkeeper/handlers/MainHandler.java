package com.secretkeeper.handlers;

import com.secretkeeper.dto.SetMasterKeyRequest;
import com.secretkeeper.dto.SimpleSecretCreateRequest;
import com.secretkeeper.dto.SimpleSecretGetRequest;
import com.secretkeeper.entities.SimpleSecret;
import com.secretkeeper.services.SimpleSecretService;
import com.secretkeeper.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/secret")
public class MainHandler {

    private final SimpleSecretService simpleSecretService;
    private final UserService userService;

    @PostMapping("/simple/create")
    public ResponseEntity<?> simpleSecretAdd(@RequestBody SimpleSecretCreateRequest request){
        if(!userService.isMasterKeyValid(request.getMasterKey())){
            return ResponseEntity.badRequest().body("Master Key is invalid!");
        }
        SimpleSecret savedSecret = simpleSecretService.saveSecret(request);
        return new ResponseEntity<>(savedSecret, HttpStatus.CREATED);
    }

    @PostMapping("/masterKey")
    public ResponseEntity<?> setMasterKey(@RequestBody SetMasterKeyRequest request){
        return userService.setMasterKey(request.getMasterKey());
    }

    @PostMapping("/simple/get")
    public ResponseEntity<?> getSimpleSecret(@RequestBody SimpleSecretGetRequest request){
        if(!userService.isMasterKeyValid(request.getMasterKey())){
            return ResponseEntity.badRequest().body("Master Key is invalid!");
        }
        return simpleSecretService.getSecret(request.getSecretId(), request.getMasterKey());
    }

    @GetMapping("/users")
    public String usersEndPoint() {
        return "ONLY users can see this";
    }
}
