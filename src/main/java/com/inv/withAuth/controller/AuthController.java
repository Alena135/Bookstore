package com.inv.withAuth.controller;

import com.inv.withAuth.model.MyUser;
import com.inv.withAuth.repository.MyUserRepository;
import com.inv.withAuth.service.MyUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;

/**
 * Controller responsible for handling authentication-related endpoints, such as login, registration, and home page.
 */
@Controller
public class AuthController {

    private final MyUserService userService;
    private final MyUserRepository userRepository;

    /**
     * Constructor that initializes the AuthController with the necessary services and repository.
     * @param userService the service for handling user-related operations
     * @param userRepository the repository for accessing user data
     */
    public AuthController(MyUserService userService, MyUserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * Returns the login page view.
     * @return the login page view name
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Displays the registration form for a new user.
     * @param model the Model object used to pass data to the view
     * @return the registration page view name
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new MyUser());
        return "register";
    }

    /**
     * Handles the registration of a new user.
     * @param user the user data submitted via the registration form
     * @param isAdmin flag indicating whether the user should be an admin
     * @return the redirect URL depending on the result of the registration
     */
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") MyUser user,
                               @RequestParam(value = "isAdmin", required = false) boolean isAdmin) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "redirect:/register?error";
        }
        try {
            MyUser savedUser = userService.registerUser(user, isAdmin);
            System.out.println("User saved with ID: " + savedUser.getId()); // Debugging
            return "redirect:/login?registered";
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
            return "redirect:/register?error";
        }
    }

    /**
     * Displays the home page for the authenticated user.
     * @param model the Model object used to pass data to the view
     * @param authentication the current authentication object containing user information
     * @return the home page view name
     */
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

