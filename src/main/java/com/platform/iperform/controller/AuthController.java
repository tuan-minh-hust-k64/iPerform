package com.platform.iperform.controller;

import com.platform.iperform.common.dto.request.AuthRequest;
import com.platform.iperform.common.dto.response.AuthResponse;
import com.platform.iperform.common.dto.response.JwtResponse;
import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.model.Permission;
import com.platform.iperform.security.jwt.JwtUtils;
import com.platform.iperform.security.services.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/auth")
@Slf4j
public class AuthController {
    private final FunctionHelper functionHelper;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthController(FunctionHelper functionHelper, JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.functionHelper = functionHelper;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserId(), authRequest.getUserId()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity
                .ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
    }
    @PostMapping(value = "/fake")
    public ResponseEntity<AuthResponse> fakeAuthGG(@RequestBody AuthRequest authRequest) {
        log.info(authRequest.toString());
        return ResponseEntity.ok(AuthResponse.builder()
                        .user_id("73a20c58-3b49-467f-90dc-3b48e4e0b75d")
                .build());
    }
}
