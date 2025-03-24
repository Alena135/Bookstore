package com.inv.noAuth.service;


import com.inv.noAuth.model.MyUser;
import com.inv.noAuth.repository.MyUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * This service implements Spring Security's {@link UserDetailsService} interface to provide
 * custom logic for loading user details by their username. It interacts with the {@link MyUserRepository}
 * to retrieve user data from the database and maps it to a Spring Security {@link UserDetails} object.
 */
 @Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MyUserRepository myUserRepository;

    /**
     * Constructs a new instance of {@link CustomUserDetailsService}.
     * @param myUserRepository the repository to fetch user data from the database
     */
    public CustomUserDetailsService(MyUserRepository myUserRepository) {
        this.myUserRepository = myUserRepository;
    }

    /**
     * Loads the user details by their username. This method fetches user data from the database
     * and creates a {@link UserDetails} object for authentication purposes in Spring Security.
     * @param username the username of the user to be loaded
     * @return a {@link UserDetails} object with the user's username, password, and authorities
     * @throws UsernameNotFoundException if the user cannot be found by the given username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser myUser = myUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        System.out.println("Found user: " + myUser.getUsername() +
                ", Role: " + myUser.getRole() +
                ", ID: " + myUser.getId());

        return org.springframework.security.core.userdetails.User
                .withUsername(myUser.getUsername())
                .password(myUser.getPassword())
                .authorities(myUser.getRole().toString())
                .build();
    }
}
