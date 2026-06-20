package com.mentorship.food_delivery_app.auth.service.implementation;

import com.mentorship.food_delivery_app.auth.dto.CustomerRegistrationDto;
import com.mentorship.food_delivery_app.auth.service.contract.AuthService;
import com.mentorship.food_delivery_app.auth.dto.LoginDto;
import com.mentorship.food_delivery_app.customer.service.contract.CustomerAuthService;
import com.mentorship.food_delivery_app.customer.service.contract.CustomerService;
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
    private final CustomerAuthService customerAuthService;

    @Override
    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password()));

        UserDetails principal = (UserDetails) authentication.getPrincipal();

        return jwtService.generateToken(principal);
    }

    @Override
    public String register(CustomerRegistrationDto customerRegistrationDto) {
        return customerAuthService.register(customerRegistrationDto);
    }
}
