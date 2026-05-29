
package com.example.identityandtenantmanagementservice.repository;

import com.example.identityandtenantmanagementservice.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
}
