package com.example.identityandtenantmanagementservice.repository;

import com.example.identityandtenantmanagementservice.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, String> {
}