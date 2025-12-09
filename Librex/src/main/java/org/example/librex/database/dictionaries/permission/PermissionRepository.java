package org.example.librex.database.dictionaries.permission;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    Optional<Permission> findByRole(Role role);
}
