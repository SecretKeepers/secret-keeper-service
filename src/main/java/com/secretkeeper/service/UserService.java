package com.secretkeeper.service;

import com.secretkeeper.entity.User;
import com.secretkeeper.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService{
    @Autowired
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public User save(User newUser) {
        return userRepository.save(newUser);
    }

//    public User authenticateUser(String username, String password) {
//        User user = userRepository.findByUsername(username);
//        if (user != null) {
//            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//            if (encoder.matches(password, user.getPassword())) {
//                return user;
//            }
//        }
//        return null;
//    }
}

