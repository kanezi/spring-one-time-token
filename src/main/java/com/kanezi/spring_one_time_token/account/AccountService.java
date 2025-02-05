package com.kanezi.spring_one_time_token.account;

import lombok.Value;
import org.springframework.security.authentication.ott.GenerateOneTimeTokenRequest;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@Value
public class AccountService {

    AccountEmailsService accountEmailsService;
    OneTimeTokenService oneTimeTokenService;
    UserDetailsManager userDetailsService;

    void requestResetPassword(String email) {
        if (!userDetailsService.userExists(email)) {
            throw new UsernameNotFoundException(email);
        }
        accountEmailsService.sendAccountResetPasswordEmail(email, createOttLink(email, "/account/reset-password"));
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
