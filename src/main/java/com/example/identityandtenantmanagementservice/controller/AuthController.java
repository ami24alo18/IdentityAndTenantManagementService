package com.example.identityandtenantmanagementservice.controller;

import com.example.identityandtenantmanagementservice.dto.LoginRequest;
import com.example.identityandtenantmanagementservice.dto.LoginResponse;
import com.example.identityandtenantmanagementservice.dto.RegisterTenantRequest;
import com.example.identityandtenantmanagementservice.model.Tenant;
import com.example.identityandtenantmanagementservice.model.User;
import com.example.identityandtenantmanagementservice.model.UserRole;
import com.example.identityandtenantmanagementservice.security.JwtUtil;
import com.example.identityandtenantmanagementservice.service.TenantService;
import com.example.identityandtenantmanagementservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final TenantService tenantService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(TenantService tenantService, UserService userService, JwtUtil jwtUtil) {
        this.tenantService = tenantService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register-tenant")
    @Operation(summary = "Register a new tenant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tenant registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<Void> registerTenant(@RequestBody RegisterTenantRequest request) {
        Tenant tenant = tenantService.createTenant(request.getTenantId(), request.getCompanyName());
        userService.createUser(tenant, request.getAdminUsername(), request.getAdminPassword(), request.getAdminEmail(), UserRole.COMPANY_ADMIN);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    @Operation(summary = "User login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        User user = userService.findByTenantIdAndUsername(request.getTenantId(), request.getUsername());
        if (userService.checkPassword(request.getPassword(), user.getPasswordHash())) {
            String token = jwtUtil.generateToken(user);
            return ResponseEntity.ok(new LoginResponse(token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}