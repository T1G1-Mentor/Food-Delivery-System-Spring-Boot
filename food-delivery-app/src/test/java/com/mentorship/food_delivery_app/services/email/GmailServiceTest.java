package com.mentorship.food_delivery_app.services.email;

import com.mentorship.food_delivery_app.common.services.implementation.GmailService;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class GmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private GmailService gmailService;
    @Captor
    private ArgumentCaptor<MimeMessage> messageCaptor;

    private String to  ;
    private String subject  ;
    private String body  ;
    private MimeMessage realMimeMessage ;

    @BeforeEach
    public void setup(){
         to = "user@test.com";
         subject = "Welcome!";
         body = "Thanks for signing up.";
         realMimeMessage = new JavaMailSenderImpl().createMimeMessage();
        when(mailSender.createMimeMessage()).thenReturn(realMimeMessage);
    }

    @Test
    @DisplayName("""
            GIVEN: valid email parameters
            WHEN: sendEmailAsync is called
            THEN: it should send the email asynchronously
            AND: Logger should print attempt message
            AND: Logger should print success message
            """)
    void sendEmailAsync_ShouldSendEmailSuccessfully(CapturedOutput output) throws Exception {
        gmailService.sendEmailAsync(to, subject, body);

        verify(mailSender, timeout(2000).times(1)).send(realMimeMessage);

        assertTrue(output.getOut().contains("Attempting to send email to user@test.com"));
        assertTrue(output.getOut().contains("Successfully sent email to user@test.com"));
    }

    @Test
    @DisplayName("""
            GIVEN: the mail server throws an exception
            WHEN: sendEmailAsync is called
            THEN: it should gracefully handle the error without throwing to the caller
            AND: Logger should print attempt message
            AND: Logger should print failure message
            """)
    void sendEmailAsync_ShouldHandleException_WhenSendingFails(CapturedOutput output) {

        doThrow(new MailSendException("SMTP connection failed"))
                .when(mailSender).send(any(MimeMessage.class));

        assertDoesNotThrow(() -> gmailService.sendEmailAsync(to, subject, body));

        verify(mailSender, timeout(2000).times(1)).send(realMimeMessage);

        assertTrue(output.getOut().contains("Attempting to send email to"));
        assertTrue(output.getOut().contains("CRITICAL: Failed to send email to"));


    }

}
