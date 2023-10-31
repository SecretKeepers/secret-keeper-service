package com.secretkeeper.services;

import com.secretkeeper.dto.JwtAuthenticationResponse;
import com.secretkeeper.dto.SignInRequest;
import com.secretkeeper.dto.SignUpRequest;
import com.secretkeeper.entities.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final CustomUserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<UserDetails> signup(SignUpRequest request) {
        User user = User
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .masterKey(null)
                .build();
        User userData = userService.saveUser(user);
        return ResponseEntity.ok(userData);
    }

    public JwtAuthenticationResponse signin(SignInRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        if (user == null) {
            throw new IllegalArgumentException("Invalid email or password.");
        }
        String jwt = jwtService.generateToken(user);
        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setMaxAge(21600); // expires in 6 hours
        //cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/"); // Global
        response.addCookie(cookie);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    public void logout(String token) {
        jwtService.blacklistToken(token);
    }

}
