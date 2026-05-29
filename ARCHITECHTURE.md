# Functional Specification: Identity & Tenant Management Service

## 1. Core Objective
Manage the global software-as-a-service (SaaS) control plane. This service handles new client company onboarding (tenants), system subscription/license verification, administrative user creation, and user authentication via cryptographically secure JSON Web Tokens (JWT).

## 2. Core Architecture & Database Bounds
* **Framework:** Java Spring Boot 3.x with Spring Security and Spring Data JPA.
* **Database Namespace:** Must operate exclusively inside a dedicated, isolated master relational schema named `tenant_auth` within the master PostgreSQL database.
* **Database Migration Requirement:** Include a database schema migration tool (like Flyway) to automatically create the master authentication tables on startup.

## 3. Data Model Requirements (Schema: `tenant_auth`)
The AI Agent must generate schema migrations and entities for two tables:
1. **`tenants` Table:**
    * Fields: Alphanumeric unique ID (Primary Key, e.g., 'company_a'), Company Name, Cryptographic License Key string, Operational Status (e.g., ACTIVE, SUSPENDED), Maximum Employee Count Limit (default 500), and Creation/Update Timestamps.
2. **`users` Table:**
    * Fields: Auto-incrementing Long ID (Primary Key), Tenant ID (Foreign Key referencing tenants.id), Username, Password Hash (must support BCrypt), Email, System Role (e.g., SUPER_ADMIN, COMPANY_ADMIN, HR), Activity Status flag, and Creation Timestamp.
    * Constraint: A composite unique constraint must be placed on `(tenant_id, username)` to prevent duplicate users within the same company while allowing identical usernames across different companies.

## 4. API Endpoints to Implement (Swagger Documented)
1. **Tenant Registration (`POST /api/v1/auth/register-tenant`):**
    * Ingestion: Accepts JSON with requested Tenant ID, Company Name, Admin Username, Admin Password, and Admin Email.
    * Logic: Validates if the Tenant ID is unique. If unique, generates an internal subscription license key, inserts the tenant record, hashes the admin password using BCrypt, and inserts the administrative user record into the database.
2. **User Authentication (`POST /api/v1/auth/login`):**
    * Ingestion: Accepts JSON containing Tenant ID, Username, and Raw Password.
    * Logic: Looks up the user within the specified Tenant ID scope. Verifies the password against the stored BCrypt hash. If valid, generates and returns a signed JWT token containing claims for `tenantId`, `username`, and `role`.