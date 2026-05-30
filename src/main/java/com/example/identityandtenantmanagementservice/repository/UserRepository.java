package com.example.identityandtenantmanagementservice.repository;

import com.example.identityandtenantmanagementservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByTenantIdAndUsername(String tenantId, String username);
}