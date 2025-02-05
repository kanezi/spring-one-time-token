package com.kanezi.spring_one_time_token.account;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/account")
@Value
public class AccountController {

    AccountService accountService;

    @GetMapping(value = {"", "/sign-in", "/login"})
    String showSignInForm(@RequestParam(required = false, defaultValue = "true") boolean error) {
        return "account/sign-in";
    }

    @GetMapping("/logout")
    String showLogoutForm() {
        return "account/logout";
    }


    @GetMapping("/forgot-password")
    String forgotPassword() {
        return "account/forgot-password";
    }

    @PostMapping("/password-reset")
    String requestPasswordReset(String email, RedirectAttributes attributes) {
        accountService.requestResetPassword(email);
        attributes.addFlashAttribute("repeatActionPart", "forgot-password");
        attributes.addFlashAttribute("message", "We have sent you mail to reset your password!");
        return "redirect:/account/ott-sent";
    }


    @GetMapping("/ott-sent")
    String ottLinkSent() {
        return "account/ott-sent";
    }

    record OttReason(String title, String action, String reason){}

    @GetMapping("/reset-password")
    String showOttSubmitForm(@RequestParam(value = "token") String token, Model model) {
        model.addAttribute("ottReason", new OttReason("Password reset", "Reset password", "change-password"));
        //return "redirect:/login/ott?token="+token;
        return "/account/ott-login";
    }


    record CreateAccountForm(@Email String email, String password, String confirmPassword){}

    @GetMapping("/register")
    String showRegistrationForm(@ModelAttribute CreateAccountForm createAccountForm) {
        return "account/sign-up";
    }

    @PostMapping("/register")
    String registerAccount(@Valid CreateAccountForm createAccountForm, RedirectAttributes attributes) {
        try {
            accountService.registerAccount(createAccountForm.email, createAccountForm.password);
        } catch (IllegalArgumentException e) {
            attributes.addFlashAttribute("createAccountForm", createAccountForm);
            attributes.addFlashAttribute("error", e.getLocalizedMessage());
            return "redirect:/account/register";
        }
        attributes.addFlashAttribute("repeatActionPart", "register");
        attributes.addFlashAttribute("message", "We have sent you mail to activate your account!");

        return "redirect:/account/ott-sent";
    }

    @GetMapping("/activate")
    String activateAccount(Model model) {
        model.addAttribute("ottReason", new OttReason("Activate account", "Activate account", "activate"));
        return "account/ott-login";
    }

    @PostMapping("/activate")
    String enableAccount() {
        accountService.enableAccount();
        return "redirect:/user?activated";
    }

}
