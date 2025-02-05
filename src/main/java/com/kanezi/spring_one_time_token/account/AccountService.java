package com.kanezi.spring_one_time_token.account;

import lombok.Value;
import org.springframework.security.authentication.ott.GenerateOneTimeTokenRequest;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Optional;

@Service
@Value
public class AccountService {

    AccountEmailsService accountEmailsService;
    OneTimeTokenService oneTimeTokenService;
    UserDetailsManager userDetailsService;
    PasswordEncoder encoder;

    void requestResetPassword(String email) {
        if (!userDetailsService.userExists(email)) {
            throw new UsernameNotFoundException(email);
        }
        accountEmailsService.sendAccountResetPasswordEmail(email, createOttLink(email, "/account/reset-password"));
    }


    void registerAccount(String email, String password) {
        if (userDetailsService.userExists(email)) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            if (userDetails.isEnabled()) {
                throw new IllegalArgumentException("active user " + email + " already exists!");
            }

            userDetailsService.deleteUser(email);
        }

        UserDetails newUser = User.builder()
                .username(email)
                .password(encoder.encode(password))
                .disabled(true)
                .build();
        userDetailsService.createUser(newUser);
        accountEmailsService.sendAccountActivationEmail(newUser, createOttLink(email, "/account/activate"));
    }


    void enableAccount() {
        SecurityContext context = SecurityContextHolder.getContext();

        Optional.ofNullable(context.getAuthentication())
                .ifPresentOrElse(a -> {
                    UserDetails principal = (UserDetails) a.getPrincipal();
                    UserDetails disabledUser = userDetailsService.loadUserByUsername(principal.getUsername());
                    UserDetails enabledUser = User.builder()
                            .authorities(disabledUser.getAuthorities())
                            .username(disabledUser.getUsername())
                            .password(disabledUser.getPassword())
                            .disabled(false)
                            .build();
                    userDetailsService.updateUser(enabledUser);
                }, () -> {
                    throw new IllegalStateException("User not logged in!");
                });
    }


    private String createOttLink(String email, String relativePath) {
        OneTimeToken oneTimeToken = oneTimeTokenService.generate(new GenerateOneTimeTokenRequest(email));
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(relativePath)
                .queryParam("token", oneTimeToken.getTokenValue())
                .build()
                .toUriString();
    }
}
