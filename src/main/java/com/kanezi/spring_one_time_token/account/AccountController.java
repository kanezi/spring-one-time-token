package com.kanezi.spring_one_time_token.account;

import lombok.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/account")
@Value
public class AccountController {

    AccountService accountService;

    @GetMapping(value = {"", "/sign-in", "/login"})
    String showSignInForm(@RequestParam(required = false, defaultValue = "true") boolean error) {
        return "account/sign-in";
    }

    @GetMapping("/forgot-password")
    String forgotPassword() {
        return "account/forgot-password";
    }

    @PostMapping("/password-reset")
    String requestPasswordReset(String email) {
        accountService.requestResetPassword(email);
        return "redirect:/account/ott-sent";
    }


    @GetMapping("/ott-sent")
    String ottLinkSent() {
        return "account/ott-sent";
    }

    @GetMapping("/reset-password")
    String showOttSubmitForm(@RequestParam(value = "token") String token) {
        return "redirect:/login/ott?token="+token;
    }

}
