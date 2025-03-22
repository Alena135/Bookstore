package com.bookstore.inventory.service;

import com.bookstore.inventory.model.MyUser;
import com.bookstore.inventory.repository.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MyUserService {


    private final MyUserRepository myUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MyUserService(MyUserRepository myUserRepository, PasswordEncoder passwordEncoder) {
        this.myUserRepository = myUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

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