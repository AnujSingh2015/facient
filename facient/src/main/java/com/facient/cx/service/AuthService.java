package com.facient.cx.service;

import com.facient.cx.config.mysql.repo.UserRepository;
import com.facient.cx.entity.mysql.User;
import com.facient.cx.exception.AuthenticationFailedException;
import com.facient.cx.exception.UserNotFoundException;
import com.facient.cx.exception.UsernameAlreadyExistsException;
import com.facient.cx.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements org.springframework.security.core.userdetails.UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final AuthenticationConfiguration authConfig;

    public AuthService(JwtUtil jwtUtil,
                       UserRepository userRepository,
                       AuthenticationConfiguration authConfig) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.authConfig = authConfig;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> org.springframework.security.core.userdetails.User
                        .withUsername(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole())
                        .build())
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public String authenticate(String username, String password) throws Exception {

        AuthenticationManager authenticationManager = authConfig.getAuthenticationManager();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        if (authentication.isAuthenticated()) {
            return jwtUtil.generateToken(username);
        } else {
            throw new AuthenticationFailedException("Authentication failed: Invalid username or password");
        }
    }

    // Return security question or null if not found
    public String getSecurityQuestion(String username) {

        return userRepository.findByUsername(username)
                .map(User::getSecurityQuestion)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
    }

    // Verify security answer; assumes stored answer is hashed with passwordEncoder
    public boolean verifySecurityAnswer(String username, String answer) {
        LOGGER.info("UserName: {} Answer: {} ", username, answer);
        return userRepository.findByUsername(username)
                .map(user -> {
                    String stored = user.getSecurityAnswer();
                    if (stored == null) return false;
                    // If answers are stored hashed:
                    if (stored.startsWith("$2a$") || stored.startsWith("$2b$")) {
                        return passwordEncoder.matches(answer.trim(), stored);
                    } else {
                        // fallback plain / case-insensitive compare (not recommended)
                        return stored.equalsIgnoreCase(answer.trim());
                    }
                })
                .orElse(false);
    }

    // Reset the password (hashes before saving). Returns true if updated.
    public boolean resetPassword(String username, String newPassword) {

        return userRepository.findByUsername(username)
                .map(user -> {
                    String hashed = passwordEncoder.encode(newPassword);
                    user.setPassword(hashed);
                    userRepository.save(user);
                    return true;
                })
                .orElse(false);
    }

    public void setSecurityAnswer(User user, String plainAnswer) {

        user.setSecurityAnswer(passwordEncoder.encode(plainAnswer));
        userRepository.save(user);
    }

    public boolean checkUserExists(String username) {

        return userRepository.existsByUsername(username);
    }

    public User registerUser(User user) {

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyExistsException(user.getUsername());
        }
        return userRepository.save(user);
    }
}