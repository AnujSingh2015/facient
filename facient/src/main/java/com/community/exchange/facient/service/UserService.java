package com.community.exchange.facient.service;

import com.community.exchange.facient.config.mysql.repo.UserRepository;
import com.community.exchange.facient.entity.mysql.User;
import com.community.exchange.facient.entity.request.SignupRequest;
import org.apache.kafka.common.errors.DuplicateResourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(isolation = Isolation.SERIALIZABLE)
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void saveUserData(SignupRequest request) {

        // fast pre-check — reduces obvious duplicate attempts
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setSecurityQuestion(request.getSecurityQuestion());
        user.setSecurityAnswer(request.getSecurityAnswer());

        //userRepository.save(user);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            // Extract PostgreSQL root cause message
            String message = ex.getMostSpecificCause() != null
                    ? ex.getMostSpecificCause().getMessage()
                    : ex.getMessage();
            // Java 21 switch with pattern matching
            throw switch (message) {
                case String msg when msg.contains("uq_user_username") ->
                        new DuplicateResourceException("Username already exists");

                case String msg when msg.contains("uq_user_email") ->
                        new DuplicateResourceException("Email already exists");

                default ->
                        new DuplicateResourceException("User with given username or email already exists");
            };
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByUsername(email);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public String findSecurityAnswerByUsername(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            return userRepository.findByUsername(username).get().getSecurityAnswer();
        }
       return null;
    }

}
