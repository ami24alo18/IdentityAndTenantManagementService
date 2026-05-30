package com.example.identityandtenantmanagementservice.service;

import com.example.identityandtenantmanagementservice.model.Tenant;
import com.example.identityandtenantmanagementservice.model.TenantStatus;
import com.example.identityandtenantmanagementservice.repository.TenantRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TenantService {

    private final TenantRepository tenantRepository;

    public TenantService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    public Tenant createTenant(String tenantId, String companyName) {
        if (tenantRepository.existsById(tenantId)) {
            throw new IllegalArgumentException("Tenant ID already exists");
        }
        Tenant tenant = new Tenant();
        tenant.setId(tenantId);
        tenant.setCompanyName(companyName);
        tenant.setLicenseKey(UUID.randomUUID().toString());
        tenant.setStatus(TenantStatus.ACTIVE);
        return tenantRepository.save(tenant);
    }
}