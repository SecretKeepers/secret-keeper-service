package com.secretkeeper.services;

import com.secretkeeper.entities.User;
import com.secretkeeper.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(BCryptPasswordEncoder.class);
        userService = new UserService(userRepository, passwordEncoder);

        // Mock SecurityContextHolder and Authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser"); // Set a valid username
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testSetMasterKey() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        when(passwordEncoder.encode(any())).thenReturn("encodedMasterKey");

        // Act
        ResponseEntity<?> responseEntity = userService.setMasterKey("newMasterKey");

        // Assert
        assertEquals(ResponseEntity.ok("MasterKey has been set successfully."), responseEntity);
        assertEquals("encodedMasterKey", user.getMasterKey());
    }

    @Test
    void testSetMasterKeyWhenMasterKeyAlreadySet() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        user.setMasterKey("encodedMasterKey");
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        // Act
        ResponseEntity<?> responseEntity = userService.setMasterKey("newMasterKey");

        // Assert
        assertEquals(ResponseEntity.badRequest().body("MasterKey has already been set and cannot be updated."), responseEntity);
        assertEquals("encodedMasterKey", user.getMasterKey());
    }

    @Test
    void testIsMasterKeyValid() {
        // Arrange
        User user = new User();
        user.setMasterKey("encodedMasterKey");
        when(userRepository.findByUsername(any())).thenReturn(user);
        when(passwordEncoder.matches("validMasterKey", "encodedMasterKey")).thenReturn(true);

        // Act
        boolean isValid = userService.isMasterKeyValid("validMasterKey");

        // Assert
        assertTrue(isValid);
    }
}
