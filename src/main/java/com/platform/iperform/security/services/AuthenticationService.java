package com.platform.iperform.security.services;

import com.platform.iperform.security.jwt.ApiKeyAuthentication;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationService {
    private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";
    private static final String AUTH_TOKEN = "ikameg1VNimEmMs67QsQGVWEmegKnId2c7Oz6APvltv9SjCOtiU4SqzXT90ZMQRj6uwZEJhpDDKZKGJX8UCA0oGgfoEwrW183l9gPdXNiYY5qunm17rpfUD5NrcCoiY9";

    public static Authentication getAuthentication(HttpServletRequest request) {
        String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        if (apiKey == null || !apiKey.equals(AUTH_TOKEN)) {
            throw new BadCredentialsException("Invalid API Key");
        }

        return new ApiKeyAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES);
    }
}
