package com.inv.withAuth.config;

import com.inv.withAuth.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration class that sets up Spring Security for the application.
 * This configuration handles user authentication, authorization, and access control.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // The service for loading user-specific data during authentication
    private final CustomUserDetailsService userDetailsService;

    /**
     * Constructor that initializes the service for handling user authentication.
     * @param userDetailsService the service for loading user details
     */
    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configures the security filter chain, including authentication and authorization rules.
     * @param http the HttpSecurity object to configure security settings
     * @return the configured SecurityFilterChain
     * @throws Exception if there is an error in the security configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/login", "/register", "/css/**", "/js/**").permitAll() // Public resources

                                .requestMatchers(HttpMethod.GET, "/books/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/books/search/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/books-web/view-all").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/books-web/{id}").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/books-web/search").hasAnyRole("USER", "ADMIN")

                                .requestMatchers(HttpMethod.POST, "/books").hasRole("ADMIN") // Only admin can create
                                .requestMatchers(HttpMethod.PUT, "/books/**").hasRole("ADMIN") // Only admin can update
                                .requestMatchers(HttpMethod.DELETE, "/books/**").hasRole("ADMIN") // Only admin can delete
                                .requestMatchers("/books-web/create", "/books-web/**/edit", "/books-web/**/delete").hasRole("ADMIN") // Only admin can modify

                                .anyRequest().authenticated() // Any other request needs authentication
                )
                .formLogin(form -> form
                        .loginPage("/login") // Custom login page
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/home", true) // Default redirect after login
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                );

        return http.build();
    }

    /**
     * Provides the authentication manager used to authenticate users.
     * @param authConfig the configuration for authentication
     * @return the AuthenticationManager instance
     * @throws Exception if there is an error in the authentication configuration
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Configures the DaoAuthenticationProvider for authenticating users from the database.
     * @return the DaoAuthenticationProvider with custom user details service and password encoder
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Configures the password encoder used for encoding and validating passwords.
     * @return the BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}