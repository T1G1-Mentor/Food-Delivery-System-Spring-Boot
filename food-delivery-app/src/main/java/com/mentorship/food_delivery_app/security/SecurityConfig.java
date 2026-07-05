package com.mentorship.food_delivery_app.security;

import com.mentorship.food_delivery_app.security.filters.FilterChainExceptionHandler;
import com.mentorship.food_delivery_app.security.filters.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final FilterChainExceptionHandler filterChainExceptionHandler;
    private final JwtAuthFilter jwtAuthFilter;
    private static final String[] OPEN_API_URLS = {
            "/v3/api-docs",
            "/v3/api-docs.yaml",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   @Qualifier("handlerExceptionResolver")
                                                   HandlerExceptionResolver exceptionResolver) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(filterChainExceptionHandler, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtAuthFilter, FilterChainExceptionHandler.class)
                .exceptionHandling(exceptions ->

                        exceptions.authenticationEntryPoint((req, res, ex) ->
                                        exceptionResolver.resolveException(req, res, null, ex)
                                )
                                .accessDeniedHandler((req, res, ex) ->
                                        exceptionResolver.resolveException(req, res, null, ex)
                                )
                )
                .authorizeHttpRequests(
                        auth ->

                                auth
                                        .requestMatchers(OPEN_API_URLS).permitAll()
                                        .requestMatchers("/api/v1/public/**").permitAll()
                                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                                        .requestMatchers("/api/v1/customers/**").hasRole("CUSTOMER")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
