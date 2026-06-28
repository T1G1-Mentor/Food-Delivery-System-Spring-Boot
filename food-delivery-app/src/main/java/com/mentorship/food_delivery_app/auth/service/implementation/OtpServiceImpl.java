package com.mentorship.food_delivery_app.auth.service.implementation;

import com.mentorship.food_delivery_app.auth.config.OtpProperties;
import com.mentorship.food_delivery_app.auth.entity.Otp;
import com.mentorship.food_delivery_app.auth.exception.OtpException;
import com.mentorship.food_delivery_app.auth.repository.OtpRepository;
import com.mentorship.food_delivery_app.auth.service.contract.OtpService;
import com.mentorship.food_delivery_app.common.dto.EmailEventRecord;
import com.mentorship.food_delivery_app.common.service.contract.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpServiceImpl implements OtpService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String INVALID_OR_EXPIRED = "OTP is invalid or has expired";

    private final OtpRepository otpRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final OtpProperties otpProperties;

    @Override
    @Transactional
    public void sendOtp(String email) {
        otpRepository.deleteByEmail(email);

        String code = generateCode();

        Otp otp = Otp.builder()
                .email(email)
                .codeHash(passwordEncoder.encode(code))
                .expiresAt(LocalDateTime.now().plusMinutes(otpProperties.expirationMinutes()))
                .used(false)
                .build();

        otpRepository.save(otp);
        log.info("OTP generated for: {}", email);

        emailService.sendEmailAsync(new EmailEventRecord(
                email,
                "Your Verification Code",
                buildEmailBody(code)
        ));
    }

    @Override
    @Transactional
    public void verifyOtp(String email, String code) {
        Otp otp = otpRepository
                .findFirstByEmailAndUsedFalseAndExpiresAtAfterOrderByCreatedAtDesc(email, LocalDateTime.now())
                .orElseThrow(() -> new OtpException(INVALID_OR_EXPIRED));

        if (!passwordEncoder.matches(code, otp.getCodeHash())) {
            throw new OtpException(INVALID_OR_EXPIRED);
        }

        otp.setUsed(true);
        log.info("OTP verified for: {}", email);
    }

    private String generateCode() {
        return String.format("%06d", SECURE_RANDOM.nextInt(1_000_000));
    }

    private String buildEmailBody(String code) {
        return String.format(
                "Your verification code is: %s%nIt will expire in %d minutes. Do not share it with anyone.",
                code, otpProperties.expirationMinutes()
        );
    }
}
