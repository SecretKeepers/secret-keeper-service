package com.secretkeeper.handlers;

import com.secretkeeper.dto.SetMasterKeyRequest;
import com.secretkeeper.dto.SignInRequest;
import com.secretkeeper.dto.SignUpRequest;
import com.secretkeeper.entities.User;
import com.secretkeeper.exceptions.AuthenticationException;
import com.secretkeeper.exceptions.UserCreationException;
import com.secretkeeper.services.AuthenticationService;
import com.secretkeeper.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AuthHandler {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest request) {
        try {
            User user = authenticationService.signup(request);
            return ResponseEntity.ok("User registered successfully with ID: " + user.getId());
        } catch (UserCreationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create user: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while registering the user.");
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SignInRequest request, HttpServletResponse response) {
        try {
            HttpStatus httpStatus = authenticationService.signin(request, response);
            if (httpStatus == HttpStatus.OK) {
                return ResponseEntity.ok("Sign in successful");
            } else {
                return ResponseEntity.status(httpStatus).body("Sign in failed!");
            }
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during sign in!");
        }
    }

    @PostMapping("/master/set")
    public ResponseEntity<?> setMasterKey(@RequestBody SetMasterKeyRequest request){
        return userService.setMasterKey(request.getMasterKey());
    }

//    @PostMapping("/master/validate")
//    public ResponseEntity<?> validateMasterKey(@RequestBody Map<String, Object> request){
//        String masterKey = (String) request.get("masterKey");
//        if(userService.isMasterKeyValid(masterKey))
//            return ResponseEntity.ok("Master Key is valid");
//        else return ResponseEntity.badRequest().body("Master Key is invalid!");
//    }

    @PostMapping("/master/add")
    public ResponseEntity<?> addMasterKeyToJWT(@RequestBody SetMasterKeyRequest masterKeyRequest,
            HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = authenticationService.addMasterKeyInJWT(masterKeyRequest.getMasterKey(), request, response);
        if (status == HttpStatus.OK) {
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.status(status).body("Invalid master key!");
        }
    }

    @GetMapping("/signout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

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
