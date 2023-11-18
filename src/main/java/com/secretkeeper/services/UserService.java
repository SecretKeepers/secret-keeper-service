package com.secretkeeper.services;

import com.secretkeeper.dto.SignInResponse;
import com.secretkeeper.entities.User;
import com.secretkeeper.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public ResponseEntity<?> setMasterKey(String masterKey){
        //TODO remove ResponseEntity from this class
        User user = this.getAuthUserFromToken();
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if (user.getMasterKey() == null) {
            user.setMasterKey(passwordEncoder.encode(masterKey));
            this.saveUser(user);
            return ResponseEntity.ok("MasterKey has been set successfully.");
        } else {
            return ResponseEntity.badRequest().body("MasterKey has already been set and cannot be updated.");
        }
    }

    public boolean isMasterKeyValid(String masterKey) {
        User user = this.getAuthUserFromToken();
        return passwordEncoder.matches(masterKey, user.getMasterKey());
    }

    public User saveUser(User newUser) {
        return userRepository.save(newUser);
    }

    public User getAuthUserFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName());
    }

    public SignInResponse getSignInResponse(String username) {
        User user = userRepository.findByUsername(username);
        return SignInResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .masterKeySet(user.getMasterKey() != null)
                .build();
    }
}

