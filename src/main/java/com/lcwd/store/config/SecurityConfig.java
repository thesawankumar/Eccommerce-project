package com.lcwd.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {
    @Bean
    public UserDetailsService userDetailsService() {
    
        //users create
        UserDetails normalUser = User.builder().username("Ankit")
                .password(passwordEncoder().encode("ankit")).roles("NORMAL").build();
        UserDetails adminUser = User.builder().username("Sawan")
                .password(passwordEncoder().encode("sawan")).roles("ADMIN").build();
//        InMemoryUserDetailsManager
        return new InMemoryUserDetailsManager(normalUser,adminUser);
    }
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
