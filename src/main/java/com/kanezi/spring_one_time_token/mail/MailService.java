package com.kanezi.spring_one_time_token.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Data;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Data
public class MailService {

    private static final String NO_REPLY_ADDRESS = "noreply@example.com";
    private final JavaMailSender javaMailSender;

    @Async
    public void sendHtml(String to,
                         String subject,
                         String htmlBody) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(NO_REPLY_ADDRESS);
        helper.setTo(to);
        helper.setSubject(subject);

        helper.setText(htmlBody, true);

        javaMailSender.send(message);
    }
}