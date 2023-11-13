package com.secretkeeper.handlers;

import com.secretkeeper.constants.Responses;
import com.secretkeeper.dto.SetMasterKeyRequest;
import com.secretkeeper.dto.SignInRequest;
import com.secretkeeper.dto.SignUpRequest;
import com.secretkeeper.exceptions.AuthenticationException;
import com.secretkeeper.exceptions.UserCreationException;
import com.secretkeeper.services.AuthenticationService;
import com.secretkeeper.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AuthHandler {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    @Autowired
    private final HttpServletRequest httpServletRequest;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest signUpRequest) {
        try {
            return ResponseEntity.ok("User registered successfully");
        } catch (UserCreationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create user: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while registering the user.");
        }
    }

    @GetMapping("/username")
    public ResponseEntity<?> validateUsername(@RequestParam String username) {
        if(authenticationService.userExist(username)){
            return ResponseEntity.badRequest().body("User with username "+username+" already exists!");
        }
        else return ResponseEntity.ok("Username is available");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SignInRequest signInRequest) {
        try {
            HttpStatus httpStatus = authenticationService.signin(signInRequest);
            if (httpStatus == HttpStatus.OK) {
                return ResponseEntity.ok(userService.getSignInResponse(signInRequest.getUsername()));
            } else {
                return ResponseEntity.status(httpStatus).body("Sign in failed!");
            }
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Responses.INVALID_USER_PASSWD.getMsg());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during sign in!");
        }
    }

    @PostMapping("/master/set")
    public ResponseEntity<?> setMasterKey(@RequestBody SetMasterKeyRequest setMasterKeyRequest){
        Objects.requireNonNull(setMasterKeyRequest.getMasterKey());
        return userService.setMasterKey(setMasterKeyRequest.getMasterKey());
    }

    @PostMapping("/master/add")
    public ResponseEntity<?> addMasterKeyToJWT(@RequestBody SetMasterKeyRequest masterKeyRequest) {
        HttpStatus status = authenticationService.addMasterKeyInJWT(masterKeyRequest.getMasterKey());
        if (status == HttpStatus.OK) {
            return ResponseEntity.ok("Success");
        } else if (status == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.status(status).body(Responses.INVALID_MASTER_KEY.getMsg());
        } else if (status == HttpStatus.PRECONDITION_FAILED) {
            return ResponseEntity.status(status).body("Master Key is not present!");
        } else {
            return ResponseEntity.internalServerError().body("Unknown Error!");
        }
    }

    @GetMapping("/signout")
    public ResponseEntity<String> logout() {
        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    authenticationService.logout(cookie.getValue());
                    return ResponseEntity.ok("Logout successful");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("FORBIDDEN");
    }
}
