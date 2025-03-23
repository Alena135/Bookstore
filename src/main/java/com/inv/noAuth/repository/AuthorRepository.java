package com.inv.noAuth.repository;

import com.inv.noAuth.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Author findByNameAndSurname(String name, String surname);

    boolean existsByNameAndSurname(String name, String surname);
}