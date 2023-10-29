package com.secretkeeper.handlers;

import com.secretkeeper.dto.JwtAuthenticationResponse;
import com.secretkeeper.dto.SignInRequest;
import com.secretkeeper.dto.SignUpRequest;
import com.secretkeeper.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class AuthHandler {
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public JwtAuthenticationResponse signup(@RequestBody SignUpRequest request) {
        return authenticationService.signup(request);
    }

    @PostMapping("/signin")
    public JwtAuthenticationResponse signin(@RequestBody SignInRequest request) {
        return authenticationService.signin(request);
    }

    @GetMapping("/signout")
    public HttpStatus logout(@RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        authenticationService.logout(token);
        return HttpStatus.OK;
    }
}

