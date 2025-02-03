package com.kanezi.spring_one_time_token.user;

import lombok.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@Value
public class UserController {

    @GetMapping
    String userProfile() {
        return "user/profile";
    }

}
