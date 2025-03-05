package com.library.library_management.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/books/scrape").hasRole("ADMIN")
                        .requestMatchers("/api/users").hasRole("ADMIN")
                        .requestMatchers("/api/loans/clear").hasRole("ADMIN")
                        .requestMatchers("/api/loans/trigger-overdue").hasRole("ADMIN")
                        .requestMatchers("/api/loans/**").hasRole("ADMIN")
                        .requestMatchers("/api/books/**").permitAll()
                        .requestMatchers("/api/loans").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic();
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(BCryptPasswordEncoder passwordEncoder) {
        var userDetailsService = new InMemoryUserDetailsManager();
        var admin = User.withUsername("admin")
                .password(passwordEncoder.encode("admin123")) // Use BCrypt
                .roles("ADMIN")
                .build();
        userDetailsService.createUser(admin);
        return userDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
