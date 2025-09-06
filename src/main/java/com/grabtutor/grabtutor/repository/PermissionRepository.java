package com.grabtutor.grabtutor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.grabtutor.grabtutor.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
}
