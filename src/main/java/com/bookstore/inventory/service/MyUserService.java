package com.bookstore.inventory.service;

import com.bookstore.inventory.model.MyUser;
import com.bookstore.inventory.repository.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserService {

    @Autowired
    private final MyUserRepository myUserRepository;

    private final PasswordEncoder passwordEncoder;

    public MyUserService(MyUserRepository myUserRepository, PasswordEncoder passwordEncoder) {
        this.myUserRepository = myUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public MyUser createUser(MyUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return myUserRepository.save(user);
    }

    public Optional<MyUser> findByUsername(String username) {
        return myUserRepository.findByUsername(username);
    }

    public MyUser registerUser(MyUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return MyUserRepository.save(user);
    }


}