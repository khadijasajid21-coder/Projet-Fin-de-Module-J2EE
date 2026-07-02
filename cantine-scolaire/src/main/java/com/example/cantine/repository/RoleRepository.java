package com.example.cantine.repository;

import com.example.cantine.entity.Role;
import com.example.cantine.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNom(RoleType nom);
}
