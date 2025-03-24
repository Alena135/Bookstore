package com.inv.noAuth.service;


import com.inv.noAuth.model.MyUser;
import com.inv.noAuth.repository.MyUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MyUserRepository myUserRepository;

    public CustomUserDetailsService(MyUserRepository myUserRepository) {
        this.myUserRepository = myUserRepository;
    }

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
