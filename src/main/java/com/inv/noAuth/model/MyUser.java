package com.inv.noAuth.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Represents a user entity with a unique username, password, name, surname, and role.
 * The user can have one of two roles: {@link Role#ROLE_ADMIN} or {@link Role#ROLE_USER}.
 */
@Getter
@Setter
@Entity
@Table(name = "my_user")
public class MyUser {
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
//    @SequenceGenerator(name = "user_seq", sequenceName = "user_sequence", allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name"/*, nullable = false*/)
    private String name;

    @Column(name = "surname"/*, nullable = false*/)
    private String surname;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    public MyUser() {}

    public enum Role {
        ROLE_ADMIN, ROLE_USER
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyUser myUser = (MyUser) o;
        return Objects.equals(id, myUser.id) && Objects.equals(username, myUser.username) && Objects.equals(password, myUser.password) && Objects.equals(name, myUser.name) && Objects.equals(surname, myUser.surname) && role == myUser.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, name, surname, role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", role=" + role +
                '}';
    }
}
