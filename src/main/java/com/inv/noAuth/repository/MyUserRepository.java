package com.inv.noAuth.repository;

import com.inv.noAuth.model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing {@link MyUser} entities.
 * Provides methods to query {@link MyUser} by username.
 * Extends {@link JpaRepository} for standard CRUD operations.
 */
@Repository
public interface MyUserRepository extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findByUsername(String username);
}