package com.library.library_management.security;

import com.library.library_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(authorize -> authorize
                        // Admin-only endpoints
                        .requestMatchers("/api/books/scrape").hasRole("ADMIN")
                        .requestMatchers("/api/users").hasRole("ADMIN")
                        .requestMatchers("/api/loans/clear").hasRole("ADMIN")
                        .requestMatchers("/api/loans/trigger-overdue").hasRole("ADMIN")
                        .requestMatchers("/api/loans/**").hasRole("ADMIN")
                        // User-specific endpoints
                        .requestMatchers("/api/users/me").authenticated()
                        // Public endpoints
                        .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                        .requestMatchers("/api/books/**").permitAll()
                        .requestMatchers("/api/loans").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic();
        return http.build();
    }


    // updated as per authentication
    @Bean
    public UserDetailsService userDetailsService(BCryptPasswordEncoder passwordEncoder) {
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager(admin);

        UserDetailsService dbUserDetailsService = username -> {
            System.out.println("Attempting to load user with email: " + username);
            return userService.getUserByEmail(username)
                    .map(user -> {
                        System.out.println("Found user: " + user.getEmail() + ", Password: " + user.getPassword());
                        return User.withUsername(user.getEmail())
                                .password(user.getPassword())
                                .roles(user instanceof com.library.library_management.model.Patron ? "PATRON" : "STAFF")
                                .build();
                    })
                    .orElseThrow(() -> {
                        System.out.println("User not found with email: " + username);
                        return new org.springframework.security.core.userdetails.UsernameNotFoundException("User not found with email: " + username);
                    });
        };

        return username -> {
            try {
                return inMemoryUserDetailsManager.loadUserByUsername(username);
            } catch (org.springframework.security.core.userdetails.UsernameNotFoundException e) {
                return dbUserDetailsService.loadUserByUsername(username);
            }
        };
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}