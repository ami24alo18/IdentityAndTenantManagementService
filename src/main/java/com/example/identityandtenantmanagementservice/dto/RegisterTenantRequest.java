package com.example.identityandtenantmanagementservice.dto;

import lombok.Data;

@Data
public class RegisterTenantRequest {
    private String tenantId;
    private String companyName;
    private String adminUsername;
    private String adminPassword;
    private String adminEmail;
}