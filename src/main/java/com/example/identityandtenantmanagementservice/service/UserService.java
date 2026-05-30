package com.example.identityandtenantmanagementservice.service;

import com.example.identityandtenantmanagementservice.model.Tenant;
import com.example.identityandtenantmanagementservice.model.User;
import com.example.identityandtenantmanagementservice.model.UserRole;
import com.example.identityandtenantmanagementservice.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User createUser(Tenant tenant, String username, String password, String email, UserRole role) {
        User user = new User();
        user.setTenant(tenant);
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole(role);
        return userRepository.save(user);
    }

    public User findByTenantIdAndUsername(String tenantId, String username) {
        return userRepository.findByTenantIdAndUsername(tenantId, username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}