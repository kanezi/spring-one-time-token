package com.kanezi.spring_one_time_token.account;

import com.kanezi.spring_one_time_token.mail.MailService;
import jakarta.mail.MessagingException;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@Value
public class AccountEmailsService {

    MailService mailService;
    SpringTemplateEngine templateEngine;

    void sendAccountResetPasswordEmail(String email, String passwordResetLink) {
        Context context = new Context();
        context.setVariable("passwordResetLink", passwordResetLink);

        String html = templateEngine.process("mail/reset-password", context);

        try {
            mailService.sendHtml(email, "reset password for account", html);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
