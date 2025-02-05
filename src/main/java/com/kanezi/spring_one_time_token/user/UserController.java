package com.kanezi.spring_one_time_token.user;

import lombok.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
@Value
public class UserController {

    record ChangePasswordForm(String password, String confirmPassword) { }

    @GetMapping
    String userProfile(@RequestParam(name = "chpwd", required = false, defaultValue = "true") boolean changePassword,
                       @ModelAttribute ChangePasswordForm changePasswordForm) {
        return "user/profile";
    }

}
