package com.mentorship.food_delivery_app.common.service.implementation;

import com.mentorship.food_delivery_app.common.dto.LoginDto;
import com.mentorship.food_delivery_app.common.service.contract.AuthService;
import com.mentorship.food_delivery_app.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password()));

        UserDetails principal = (UserDetails) authentication.getPrincipal();

        return jwtService.generateToken(principal);
    }
}
