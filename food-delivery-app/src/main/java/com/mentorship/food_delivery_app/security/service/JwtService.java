package com.mentorship.food_delivery_app.security.service;

import com.mentorship.food_delivery_app.security.entities.CustomerPrincipal;
import com.mentorship.food_delivery_app.security.entities.UserPrincipal;
import com.mentorship.food_delivery_app.security.exceptions.InvalidTokenException;
import com.mentorship.food_delivery_app.security.service.utils.KeyUtils;
import com.mentorship.food_delivery_app.user.entity.enums.UserType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JwtService {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    @Value("${app.security.jwt.access-token-expiration}")
    private long accessTokenExpiration;

    public JwtService() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        this.privateKey = KeyUtils.loadPrivateKey("/keys/private-key.pem");
        this.publicKey = KeyUtils.loadPublicKey("/keys/public-key.pem");
    }

    public UserDetails decodeToken(String token) {
        Claims claims = this.getClaims(token);
        String userType = claims.get(ClaimConstants.USER_TYPE.name(), String.class);
        validateValue(userType, ClaimConstants.USER_TYPE.name());

        if (userType.equals(UserType.CUSTOMER.name()))
            return buildSecurityCustomer(claims);


        return buildSecurityUser(claims);
    }

    public boolean isTokenValid(String token) {
        Claims claims = this.getClaims(token);
        String email = claims.getSubject();
        Date expiration = claims.getExpiration();
        validateValue(email, ClaimConstants.EMAIL.name());
        validateValue(expiration, ClaimConstants.EXPIRATION.name());

        return !isTokenExpired(expiration);
    }

    public String generateToken(UserDetails userDetails) {
        if (userDetails instanceof CustomerPrincipal customer)
            return generateCustomerToken(customer);

        return generateUserAdminToken((UserPrincipal) userDetails);
    }

    private UserDetails buildSecurityUser(Claims claims) {
        Collection<SimpleGrantedAuthority> authorities =getAuthorities(claims);

        Boolean isEnabled = getRequiredClaim(claims,
                ClaimConstants.IS_ENABLED, Boolean.class);


        // 4. Build the principal
        return new UserPrincipal(
                UUID.fromString(getRequiredClaim(claims, ClaimConstants.USER_ID, String.class)),
                claims.getSubject(), // Email
                "",                  // Blank password
                getRequiredClaim(claims, ClaimConstants.FIRST_NAME, String.class),
                getRequiredClaim(claims, ClaimConstants.LAST_NAME, String.class),
                UserType.USER_ADMIN,
                authorities,
                isEnabled // Defaults to false if null
        );
    }

    private UserDetails buildSecurityCustomer(Claims claims) {
        Collection<SimpleGrantedAuthority> authorities = getAuthorities(claims);

        Boolean isEnabled = getRequiredClaim(claims,
                ClaimConstants.IS_ENABLED, Boolean.class);


        // 4. Build the principal
        return new CustomerPrincipal(
                UUID.fromString(getRequiredClaim(claims, ClaimConstants.USER_ID, String.class)),
                UUID.fromString(getRequiredClaim(claims, ClaimConstants.CUSTOMER_ID, String.class)),
                claims.getSubject(), // Email
                "",                  // Blank password
                getRequiredClaim(claims, ClaimConstants.FIRST_NAME, String.class),
                getRequiredClaim(claims, ClaimConstants.LAST_NAME, String.class),
                UserType.CUSTOMER,
                authorities,
                isEnabled // Defaults to false if null
        );
    }

    private @NonNull Collection<SimpleGrantedAuthority> getAuthorities(Claims claims) {
        List<Map<String, String>> roleStrings = getRequiredClaim(claims,
                ClaimConstants.ROLES, List.class);

        return
                roleStrings.stream().map(roles->roles.get("authority"))
                        .map(SimpleGrantedAuthority::new).toList();
    }

    private <T> T getRequiredClaim(Claims claims, ClaimConstants claimName, Class<T> requiredType) {
        T value = claims.get(claimName.name(), requiredType);
        validateValue(value, claimName.name());
        return value;
    }

    private <T> void validateValue(T value, String claimName) {
        if (value == null) {
            throw new InvalidTokenException(
                    String.format("Malformed JWT: Missing or null required claim '%s'", claimName)
            );
        }
    }

    private String generateUserAdminToken(UserPrincipal user) {
        Map<String, Object> claims = Map.of(
                ClaimConstants.USER_ID.name(), user.getUserId(),
                ClaimConstants.ROLES.name(), user.getAuthorities(),
                ClaimConstants.FIRST_NAME.name(), user.getFirstName(),
                ClaimConstants.LAST_NAME.name(), user.getLastName(),
                ClaimConstants.USER_TYPE.name(), user.getUserType(),
                ClaimConstants.IS_ENABLED.name(), user.isEnabled());

        return buildAccessToken(user.getUsername(), claims);
    }

    private String generateCustomerToken(CustomerPrincipal customer) {
        Map<String, Object> claims = Map.of(
                ClaimConstants.USER_ID.name(), customer.getUserId(),
                ClaimConstants.CUSTOMER_ID.name(), customer.getCustomerId(),
                ClaimConstants.ROLES.name(), customer.getAuthorities(),
                ClaimConstants.FIRST_NAME.name(), customer.getFirstName(),
                ClaimConstants.LAST_NAME.name(), customer.getLastName(),
                ClaimConstants.USER_TYPE.name(), customer.getUserType(),
                ClaimConstants.IS_ENABLED.name(), customer.isEnabled());

        return buildAccessToken(customer.getUsername(), claims);
    }

    private String buildAccessToken(String email, Map<String, Object> claims) {
        Instant now = Instant.now();
        Instant expiration = now.plus(accessTokenExpiration, ChronoUnit.MILLIS);

        return Jwts.builder()
                .signWith(privateKey)
                .subject(email)
                .claims(claims)
                .expiration(Date.from(expiration))
                .issuedAt(Date.from(now))
                .compact();
    }

    private boolean isTokenExpired(Date expiration) {
        return expiration.before(Date.from(Instant.now()));
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private enum ClaimConstants {
        USER_ID,
        CUSTOMER_ID,
        ROLES,
        FIRST_NAME,
        LAST_NAME,
        USER_TYPE,
        IS_ENABLED,
        EMAIL,
        EXPIRATION
    }
}
