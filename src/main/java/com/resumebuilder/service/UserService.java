package com.resumebuilder.service;

import com.resumebuilder.entity.Role;
import com.resumebuilder.entity.User;
import com.resumebuilder.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    /** Returns the authenticated user if the email/password pair is valid. */
    public Optional<User> authenticate(String email, String rawPassword) {
        return userRepository.findByEmail(email)
                .filter(u -> matches(rawPassword, u.getPassword()));
    }

    private boolean matches(String rawPassword, String storedPassword) {
        // The seeded admin account (application.properties) is stored as plain text
        // for a zero-setup demo login; every registered user's password is BCrypt-hashed.
        if (!storedPassword.startsWith("$2a$") && !storedPassword.startsWith("$2b$")) {
            return rawPassword.equals(storedPassword);
        }
        return passwordEncoder.matches(rawPassword, storedPassword);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
