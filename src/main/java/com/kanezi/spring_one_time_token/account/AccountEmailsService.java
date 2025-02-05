package com.kanezi.spring_one_time_token.account;

import com.kanezi.spring_one_time_token.mail.MailService;
import jakarta.mail.MessagingException;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@Value
@Log4j2
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
            log.error("Error while sending reset password email => {}", e.getLocalizedMessage());
        }
    }

    void sendAccountActivationEmail(UserDetails user, String activationLink) {
        Context context = new Context();
        context.setVariable("activationLink", activationLink);

        String activateAccountMailHtml = templateEngine.process("mail/activate-account", context);

        try {
            mailService.sendHtml(user.getUsername(), "activate your account", activateAccountMailHtml);
        } catch (MessagingException e) {
            log.error("Error while sending account activation email => {}", e.getLocalizedMessage());
        }
    }
}
