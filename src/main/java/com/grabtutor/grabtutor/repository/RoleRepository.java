package com.grabtutor.grabtutor.repository;

import com.grabtutor.grabtutor.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
    Role findByName(String name);
}
