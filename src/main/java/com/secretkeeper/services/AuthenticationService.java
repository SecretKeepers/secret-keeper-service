package com.secretkeeper.services;

import com.secretkeeper.dto.JwtAuthenticationResponse;
import com.secretkeeper.dto.SignInRequest;
import com.secretkeeper.dto.SignUpRequest;
import com.secretkeeper.entities.User;
import com.secretkeeper.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse signup(SignUpRequest request) {
        User user = User
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User userData = userService.save(user);
        String jwt = jwtService.generateToken(userData);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    public JwtAuthenticationResponse signin(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User user = userRepository.findByUsername(request.getUsername());
        if (user == null) {
            throw new IllegalArgumentException("Invalid email or password.");
        }
        String jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    public void logout(String token) {
        jwtService.blacklistToken(token);
    }

}
