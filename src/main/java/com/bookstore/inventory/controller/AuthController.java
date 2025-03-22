package com.bookstore.inventory.controller;

import com.bookstore.inventory.model.MyUser;
import com.bookstore.inventory.repository.MyUserRepository;
import com.bookstore.inventory.service.MyUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;

import java.util.Optional;

@Controller
public class AuthController {

    private final MyUserService userService;
    private final MyUserRepository userRepository;

    public AuthController(MyUserService userService, MyUserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new MyUser());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") MyUser user,
                               @RequestParam(value = "isAdmin", required = false) boolean isAdmin) {
        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "redirect:/register?error";
        }

        try {
            // Register user with appropriate role
            MyUser savedUser = userService.registerUser(user, isAdmin);
            System.out.println("User saved with ID: " + savedUser.getId()); // Debugging
            return "redirect:/login?registered";
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
            return "redirect:/register?error";
        }
    }

    @GetMapping("/home")
    public String home(Model model, Authentication authentication) {
        // Add user role information to the model
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("username", authentication.getName());

        return "home";
    }

}


