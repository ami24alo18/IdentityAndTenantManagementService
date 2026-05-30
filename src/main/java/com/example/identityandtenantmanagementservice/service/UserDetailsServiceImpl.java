package com.example.identityandtenantmanagementservice.service;

import com.example.identityandtenantmanagementservice.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // This is a simplification. In a real multi-tenant app, you'd also need the tenant ID.
        // The tenant ID would likely be extracted from the request (e.g., subdomain, header).
        String[] parts = username.split(":");
        if (parts.length != 2) {
            throw new UsernameNotFoundException("Username must be in 'tenantId:username' format");
        }
        String tenantId = parts[0];
        String actualUsername = parts[1];

        com.example.identityandtenantmanagementservice.entity.User user = userRepository.findByTenantIdAndUsername(tenantId, actualUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + actualUsername + " for tenant: " + tenantId));

        return new User(user.getUsername(), user.getPasswordHash(), Collections.emptyList());
    }
}