package com.kanezi.spring_one_time_token.account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class AccountController {

    @GetMapping("/ott-sent")
    String ottLinkSent() {
        return "account/ott-sent";
    }
}
