package com.secretkeeper.handlers;

import com.secretkeeper.dto.SetMasterKeyRequest;
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
        if(secretRequest.getType().equalsIgnoreCase("simple")) {
            String token = jwtService.getJwtFromRequest(request);
            String masterKey = jwtService.extractClaim(token, "masterKey");
            SimpleSecret savedSecret = simpleSecretService.saveSecret(secretRequest, masterKey);
            return new ResponseEntity<>(savedSecret, HttpStatus.CREATED);
        }
        else return ResponseEntity.badRequest().body("Invalid secret type!");
    }

    @PostMapping("/get")
    public ResponseEntity<?> getSimpleSecret(@RequestBody SimpleSecretGetRequest secretRequest, HttpServletRequest request){
        if(secretRequest.getType().equalsIgnoreCase("simple")) {
            String token = jwtService.getJwtFromRequest(request);
            String masterKey = jwtService.extractClaim(token, "masterKey");
            SimpleSecretResponse secret = simpleSecretService.getSecret(secretRequest.getSecretId(), masterKey);
            return ResponseEntity.ok(secret);
        }
        return ResponseEntity.badRequest().body("Invalid secret type!");
    }

    @GetMapping("/users")
    public String usersEndPoint() {
        return "ONLY users can see this";
    }
}
