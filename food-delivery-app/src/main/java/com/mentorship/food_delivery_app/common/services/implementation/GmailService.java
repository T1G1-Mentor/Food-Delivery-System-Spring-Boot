package com.mentorship.food_delivery_app.common.services.implementation;

import com.mentorship.food_delivery_app.common.services.contract.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class GmailService implements EmailService {
    private final JavaMailSender mailSender;

    @Async("emailExecutor")
    @Override
    public void sendEmailAsync(String to, String subject, String body) {
        try {
            log.info("Attempting to send email to {}", to);
            sendEmail(to, subject, body);
            log.info("Successfully sent email to {}", to);
        } catch (MessagingException e) {
            log.error("CRITICAL: Failed to send email to {}. Reason: {}", to, e.getMessage(), e);
        }
    }

    private void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.addTo(to);
        helper.setSubject(subject);
        helper.setText(body);
        helper.setSentDate(Date.from(Instant.now()));
        mailSender.send(message);

    }
}
