package com.kanezi.spring_one_time_token.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ott.InMemoryOneTimeTokenService;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(r -> r
                        .requestMatchers("/account/**", "/error", "/").permitAll()
                        .anyRequest().authenticated())
                .formLogin(flc -> flc
                        .loginPage("/account/login")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/user")
                        .failureForwardUrl("/account/login?error")
                )
                .logout(lc -> lc
                        .logoutUrl("/account/logout")
                        .logoutSuccessUrl("/?logout"))
                .oneTimeTokenLogin(ottc -> ottc
                        .loginProcessingUrl("/account/ott-submit")
                        .showDefaultSubmitPage(false)
                        .authenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/account/login?error"))
                )
                .build();
    }

    @Bean
    OneTimeTokenService oneTimeTokenService() {
        return new InMemoryOneTimeTokenService();
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    InMemoryUserDetailsManager userDetailsService(PasswordEncoder encoder) {
        InMemoryUserDetailsManager users = new InMemoryUserDetailsManager();
        users.createUser(User.builder()
                .username("bob@example.com")
                .password(encoder.encode("bob"))
                .build());
        return users;
    }
}
