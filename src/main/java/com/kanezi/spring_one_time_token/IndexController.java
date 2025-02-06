package com.kanezi.spring_one_time_token;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
//@RequestMapping("/")
public class IndexController {

    @GetMapping("/")
    String index(@RequestParam(name = "logout", defaultValue = "true", required = false) boolean logout) {
        return "index";
    }
}
