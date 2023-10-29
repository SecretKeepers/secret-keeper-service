package com.secretkeeper.handlers;

import com.secretkeeper.dto.SimpleSecretRequest;
import com.secretkeeper.dto.SimpleSecretResponse;
import com.secretkeeper.entities.SimpleSecret;
import com.secretkeeper.entities.User;
import com.secretkeeper.repositories.SimpleSecretRepository;
import com.secretkeeper.repositories.UserRepository;
import com.secretkeeper.services.SimpleSecretService;
import com.secretkeeper.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/secret")
public class MainHandler {

    private final SimpleSecretService simpleSecretService;

    @PostMapping("/simple")
    public ResponseEntity<SimpleSecret> simpleSecretAdd(@RequestBody SimpleSecretRequest request){
        SimpleSecret savedSecret = simpleSecretService.saveSecret(request);
        return new ResponseEntity<>(savedSecret, HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public String usersEndPoint() {
        return "ONLY users can see this";
    }
}
