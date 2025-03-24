package com.inv.withAuth.repository;

import com.inv.withAuth.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Author} entities.
 * Provides a method to find an {@link Author} by their name and surname.
 * Extends {@link JpaRepository} for standard CRUD operations.
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Author findByNameAndSurname(String name, String surname);
}