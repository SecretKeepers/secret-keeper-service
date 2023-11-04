package com.secretkeeper.services;

import com.secretkeeper.dto.SignInRequest;
import com.secretkeeper.dto.SignUpRequest;
import com.secretkeeper.entities.User;
import com.secretkeeper.exceptions.AuthenticationException;
import com.secretkeeper.exceptions.UserCreationException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final CustomUserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public User signup(SignUpRequest request) {
        User user = User
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .masterKey(null)
                .build();
        try {
            return userService.saveUser(user);
        } catch (Exception e) {
            throw new UserCreationException("Failed to create the user.");
        }
    }

    public HttpStatus signin(SignInRequest request, HttpServletResponse response) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
            if (user == null) {
                throw new IllegalArgumentException("Invalid email or password.");
            }
            String jwt = jwtService.generateToken(user.getUsername());
            Cookie cookie = new Cookie("jwt", jwt);
            cookie.setMaxAge(21600); // expires in 6 hours
            //cookie.setSecure(true);
            cookie.setHttpOnly(true);
            cookie.setPath("/"); // Global
            response.addCookie(cookie);
            return HttpStatus.OK;
        } catch (AuthenticationException e) {
            throw new AuthenticationException("Invalid email or password!");
        } catch (Exception e) {
            throw new AuthenticationServiceException("An error occurred during sign in!");
        }
    }

    public HttpStatus addMasterKeyInJWT(String key, HttpServletRequest request, HttpServletResponse response) {
        if(userService.isMasterKeyValid(key)){
            Map<String, Object> extraClaims = new HashMap<>();
            User user = userService.getAuthUserFromToken();
            extraClaims.put("masterKey", user.getMasterKey());
            String jwt = jwtService.generateToken(user.getUsername(), extraClaims);
            Cookie cookie = new Cookie("jwt", jwt);
            cookie.setMaxAge(21600); // expires in 6 hours
            //cookie.setSecure(true);
            cookie.setHttpOnly(true);
            cookie.setPath("/"); // Global
            response.addCookie(cookie);
            return HttpStatus.OK;
        }
        else return HttpStatus.BAD_REQUEST;
    }

    public void logout(String token) {
        jwtService.blacklistToken(token);
    }

}
