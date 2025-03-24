package com.inv.noAuth.repository;

import com.inv.noAuth.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Genre} entities.
 * Provides methods to query {@link Genre} by its name.
 * Extends {@link JpaRepository} for standard CRUD operations.
 */
@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Genre findByName(String name);
}