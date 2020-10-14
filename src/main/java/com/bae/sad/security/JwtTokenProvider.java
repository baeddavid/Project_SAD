package com.bae.sad.security;

import io.jsonwebtoken.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    // The jwt signing key we stored in an .env file
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    // The jwt expiration time from creation stored in an .env file
    @Value("{app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    // The method for generating a jwt token
    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        // Get the current date to set when the token expires
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        // Return the built token
        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    // Method to retrieve user id from a jwt token
    public Long getUserIdFromJWT(String token) {
        Claims claims =  Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    // Method to validate a jwt token
    public boolean validateToken(String authToken) {
        try {
            // Retrieve the token and return a boolean true
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch(SignatureException ex) {
            logger.error("Invalid JWT Signature");
        } catch(MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch(ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch(UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch(IllegalArgumentException ex) {
            logger.error("JWT claims string is empty");
        }
        return false;
    }
}
