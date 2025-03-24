package com.inv.noAuth.repository;

import com.inv.noAuth.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Genre findByName(String name);

//    boolean existsByName(String name);
}