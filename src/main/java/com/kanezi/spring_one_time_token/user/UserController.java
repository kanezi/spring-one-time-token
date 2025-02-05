package com.kanezi.spring_one_time_token.user;

import jakarta.validation.Valid;
import lombok.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
@Value
public class UserController {

    UserService userService;

    record ChangePasswordForm(String password, String confirmPassword) { }

    @GetMapping
    String userProfile(@RequestParam(name = "chpwd", required = false, defaultValue = "true") boolean changePassword,
                       @ModelAttribute ChangePasswordForm changePasswordForm) {
        return "user/profile";
    }

    @PostMapping("/change-password")
    String changePassword(@Valid ChangePasswordForm changePasswordForm, RedirectAttributes attributes) {
        userService.resetPassword(changePasswordForm.password);
        attributes.addFlashAttribute("message", "Password successfully changed!");
        return "redirect:/user";
    }


}
