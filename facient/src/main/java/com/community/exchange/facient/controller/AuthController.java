package com.community.exchange.facient.controller;

import com.community.exchange.facient.config.mysql.repo.UserRepository;
import com.community.exchange.facient.config.security.PasswordConfig;
import com.community.exchange.facient.entity.mysql.User;
import com.community.exchange.facient.entity.request.LoginRequest;
import com.community.exchange.facient.entity.request.ResetPasswordRequest;
import com.community.exchange.facient.entity.request.SignupRequest;
import com.community.exchange.facient.entity.response.ApiResponse;
import com.community.exchange.facient.entity.response.JwtResponse;
import com.community.exchange.facient.entity.response.Response;
import com.community.exchange.facient.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordConfig passwordConfig;

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {

        LOGGER.info("Login Username: {}", req.getUsername());
        try {
            String token = authService.authenticate(req.getUsername(), req.getPassword());
            LOGGER.info("Token: {}", token);
            return ResponseEntity.ok(new JwtResponse(token, "Login successful"));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new JwtResponse(null, "Invalid username or password"));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest req) {
        LOGGER.info("signup: {}", req.getUsername());

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordConfig.passwordEncoder().encode(req.getPassword()));
        user.setRole(req.getRole() != null ? req.getRole() : "USER");
        user.setEmail(req.getEmail());
        user.setSecurityQuestion(req.getSecurityQuestion());
        user.setSecurityAnswer(req.getSecurityAnswer());

        User registeredUser = authService.registerUser(user);

        ApiResponse<User> response = new ApiResponse<>(true, "User registered successfully!", registeredUser);


        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgetPassword(@RequestBody LoginRequest req) {
        LOGGER.info("forgetPassword: {}", req.getUsername());
        if (userRepository.findByUsername(req.getUsername()).isEmpty()) {
            return ResponseEntity.badRequest().body("Username does not exists");
        }
        return ResponseEntity.ok("Password reset successfully!");
    }

    // Step 1: Get Security Question
    @GetMapping("/security-question/{username}")
    public ResponseEntity<?> getSecurityQuestion(@PathVariable String username) {
        LOGGER.info("getSecurityQuestion: {}", username);
        String question = authService.getSecurityQuestion(username);
        if (question != null) {
            return ResponseEntity.ok(Map.of("question", question));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    // Step 2: Verify Answer
    @PostMapping("/verify-answer")
    public ResponseEntity<?> verifyAnswer(@RequestBody Map<String, String> payload) {
        LOGGER.info("Username: {} Answer: {} ", payload.get("username"), payload.get("answer"));
        String username = payload.get("username");
        String answer = payload.get("answer");
        System.out.println("UserName: "+username+" Answer: "+answer);
        boolean valid = authService.verifySecurityAnswer(username, answer);
        if (valid) {
            Response response =  new Response(true, "Security answer validate successfully!");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid answer");
    }

    //tep 3: Reset Password
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        LOGGER.info("Reset Password: {} Username: {}", request.getNewPassword(), request.getUsername());
        // 1. Find user by username
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());

        if (userOpt.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new Response(false,"User not found"));
        }

        User user = userOpt.get();
        // 2. Encode and update password
        user.setPassword(passwordConfig.passwordEncoder().encode(request.getNewPassword()));
        userRepository.save(user);

        Response response =  new Response(true, "Password reset successfully!");
        return ResponseEntity.ok(response);
    }
}