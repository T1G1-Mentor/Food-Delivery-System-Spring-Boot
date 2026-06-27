package com.mentorship.food_delivery_app.auth.service.implementation;

import com.mentorship.food_delivery_app.auth.dto.OtpRequestDto;
import com.mentorship.food_delivery_app.auth.dto.OtpVerificationRequestDto;
import com.mentorship.food_delivery_app.auth.entities.UserOtp;
import com.mentorship.food_delivery_app.auth.exception.InvalidOtpException;
import com.mentorship.food_delivery_app.auth.repository.UserOtpRepository;
import com.mentorship.food_delivery_app.auth.service.contract.OtpService;
import com.mentorship.food_delivery_app.common.dto.EmailEventRecord;
import com.mentorship.food_delivery_app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OtpServiceImp implements OtpService {
    private final UserOtpRepository userOtpRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder encoder;
    private final SecureRandom secureRandom;

    @Transactional
    @Override
    public void requestOtp(OtpRequestDto otpRequestDto) {
        Optional<UUID> userId = userRepository.findUserIdByEmail(otpRequestDto.userEmail());
        if (userId.isEmpty()) {
            return;
        }
        userOtpRepository.revokeUserOtpByEmail(otpRequestDto.userEmail());
        String code = this.generateCode();

        UserOtp userOtp = UserOtp.builder().userId(userId.get())
                .userEmail(otpRequestDto.userEmail())
                .otpCode(encoder.encode(code))
                .otpExpiration(LocalDateTime.now().plusMinutes(15))
                .isRevoked(false)
                .build();

        userOtpRepository.save(userOtp);

        sendEmail(otpRequestDto.userEmail(), code);

    }

    @Transactional
    @Override
    public UserDetails verifyOtp(OtpVerificationRequestDto otpRequest) {
        UserOtp userOtp = getUserOtpByEmail(otpRequest.userEmail());

        if (userOtp.isExpired())
            throw new InvalidOtpException(String.format("Trial to use an expired OTP from email %s, and the given code is %s"
                    , userOtp.getUserEmail(), otpRequest.otpCode()));


        if (userOtp.isRevoked())
            throw new InvalidOtpException(String.format("Trial to use a revoked OTP from email %s, and the given code is %s"
                    , userOtp.getUserEmail(), otpRequest.otpCode()));

        if (!encoder.matches(otpRequest.otpCode(), userOtp.getOtpCode()))
            throw new InvalidOtpException(String.format("Mismatched OTP from email %s, and the given code is %s"
                    , userOtp.getUserEmail(), otpRequest.otpCode()));

        userOtp.setRevoked(true);

        return userDetailsService.loadUserByUsername(otpRequest.userEmail());
    }

    private UserOtp getUserOtpByEmail(String email) {
        return userOtpRepository.findByUserEmail(email).orElseThrow(() ->
                new InvalidOtpException
                        ("No OTP found for the user with email " + email));
    }

    private void sendEmail(String to, String code) {
        applicationEventPublisher.publishEvent(new EmailEventRecord(to, "Your OTP Code", String.format(
                "Here is your one time code, it only valid for the next 15min%n%s"
                , code)));
    }

    private String generateCode() {

        StringBuilder stringBuilder = new StringBuilder(6);

        for (int i = 0; i < 6; i++)
            stringBuilder.append(secureRandom.nextInt(10));

        return stringBuilder.toString();
    }

}
