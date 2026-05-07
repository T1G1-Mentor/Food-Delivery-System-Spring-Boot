package com.mentorship.food_delivery_app.common.services.implementation;

import com.mentorship.food_delivery_app.common.services.contract.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GmailService implements EmailService {
    private static final ExecutorService EXECUTOR_SERVICE = Executors.
            newThreadPerTaskExecutor(Thread.ofVirtual().name("E-Mail-", 1).factory());

    private final JavaMailSender mailSender;

    @Override
    public void sendEmailAsync(String to, String subject, String body) {
        CompletableFuture.runAsync(() -> {
                    try {
                        log.info("Attempting to send email to {}", to);
                        sendEmail(to, subject, body);
                        log.info("Successfully sent email to {}", to);
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                }, EXECUTOR_SERVICE)
                .exceptionally(throwable -> {
                    log.error("CRITICAL: Failed to send email to {}. Reason: {}", to, throwable.getMessage(), throwable);
                    return null;
                });
        log.info("Hello");
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
