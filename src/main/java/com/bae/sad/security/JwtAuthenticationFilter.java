package com.bae.sad.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // Injecting Jwt and Userdetails dependencies
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    // OncePerRequestFilter requires overriding doFilterInternal method
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // Grab the jwt token from the request
            String jwt = getJwtFromRequest(request);

            // Check if what we grabbed is a jwt token and a valid token
            if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                // Grab the user id from the jwt token
                Long userId = tokenProvider.getUserIdFromJWT(jwt);
                // Load user data from the user id
                UserDetails userDetails = customUserDetailService.loadUserById(userId);
                // Authenticate our user
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        // What we grabbed was either not a jwt token or not a valid jwt token
        catch(Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }
        filterChain.doFilter(request, response);
    }

    // Grabs the jwt token from a HTTP Request
    private String getJwtFromRequest(HttpServletRequest request) {
        // Grab our auth header
        String bearerToken = request.getHeader("Authorization");
        // If it is a authentication request and we have a bearer token
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // Return the jwt token
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
