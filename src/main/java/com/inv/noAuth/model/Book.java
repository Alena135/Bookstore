package com.inv.noAuth.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "book")
public class Book {
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_seq")
//    @SequenceGenerator(name = "book_seq", sequenceName = "book_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", nullable = false)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "price")
    private Long price;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "book_genre",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>(); // Each book can have multiple genres

    public Book() {}

    public Book(String title, Long price, String authorName, String authorSurname, String genre) {
        this.title = title;
        this.price = price;
        this.author = new Author(authorName, authorSurname);
        this.genres = new HashSet<>();
        this.genres.add(new Genre(genre));
    }

    public Book(Long id, String title, Long price, String authorName, String authorSurname, String genre) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.author = new Author(authorName, authorSurname);
        this.genres = new HashSet<>();
        this.genres.add(new Genre(genre));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) && Objects.equals(title, book.title) && Objects.equals(price, book.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, price);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                '}';
    }

}
