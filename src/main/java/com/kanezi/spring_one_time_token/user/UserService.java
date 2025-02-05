package com.kanezi.spring_one_time_token.user;

import lombok.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Value
public class UserService {

    InMemoryUserDetailsManager userDetailsService;
    PasswordEncoder encoder;

    void resetPassword(String newPassword) {
        SecurityContext context = SecurityContextHolder.getContext();
        Optional.ofNullable(context.getAuthentication())
                .ifPresentOrElse(authentication -> {
                    UserDetails principal = (UserDetails) authentication.getPrincipal();
                    userDetailsService.updatePassword(principal, encoder.encode(newPassword));
                }, () -> {
                    throw new IllegalStateException("User not logged in!");
                })
        ;
    }
}