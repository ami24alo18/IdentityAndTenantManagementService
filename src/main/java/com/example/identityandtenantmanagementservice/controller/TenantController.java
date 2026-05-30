package com.example.identityandtenantmanagementservice.controller;

import com.example.identityandtenantmanagementservice.model.Tenant;
import com.example.identityandtenantmanagementservice.repository.TenantRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tenants")
public class TenantController {

    private final TenantRepository tenantRepository;

    public TenantController(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @GetMapping
    @Operation(summary = "Get all tenants")
    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a tenant by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the tenant"),
            @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    public ResponseEntity<Tenant> getTenantById(@PathVariable String id) {
        return tenantRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}