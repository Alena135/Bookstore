package com.inv.withAuth.service;

import com.inv.withAuth.model.MyUser;
import com.inv.withAuth.repository.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing user-related operations, including user registration.
 */
 @Service
public class MyUserService {


    private final MyUserRepository myUserRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a new instance of {@link MyUserService}.
     * @param myUserRepository the repository for interacting with user data in the database
     * @param passwordEncoder the encoder used to encode user passwords
     */
    @Autowired
    public MyUserService(MyUserRepository myUserRepository, PasswordEncoder passwordEncoder) {
        this.myUserRepository = myUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user by encoding their password and assigning a role (either {@code ROLE_ADMIN} or {@code ROLE_USER}).
     * The user is then saved to the database.
     * @param user the {@link MyUser} object to be registered
     * @param isAdmin a flag indicating whether the user should be an admin (true) or a regular user (false)
     * @return the saved {@link MyUser} object
     */
    @Transactional
    public MyUser registerUser(MyUser user, boolean isAdmin) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(isAdmin ? MyUser.Role.ROLE_ADMIN : MyUser.Role.ROLE_USER);
        MyUser savedUser = myUserRepository.save(user);
        System.out.println("User saved with ID: " + savedUser.getId() +
                ", Username: " + savedUser.getUsername() +
                ", Role: " + savedUser.getRole());
        return savedUser;
    }
}