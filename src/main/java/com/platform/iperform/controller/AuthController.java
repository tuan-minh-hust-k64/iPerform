package com.platform.iperform.controller;

import com.platform.iperform.common.dto.request.AuthRequest;
import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.security.jwt.JwtUtils;
import com.platform.iperform.security.services.UserDetailsImpl;
import com.platform.iperform.service.MailService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.graphql.client.WebGraphQlClient;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Controller
@RequestMapping(value = "/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "https://iperform.ikameglobal.com"}, allowCredentials = "true")
@Slf4j
public class AuthController {
    private final FunctionHelper functionHelper;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final MailService mailService;

    public AuthController(FunctionHelper functionHelper, JwtUtils jwtUtils, AuthenticationManager authenticationManager, MailService mailService) {
        this.functionHelper = functionHelper;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.mailService = mailService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        Map<String, Object> authenHrm = functionHelper.authenticateHrm(authRequest);
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authenHrm.get("user_id"), authenHrm.get("user_id")));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        authenHrm.put("token", jwt);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .toList();
//        HttpHeaders responseHeaders = new HttpHeaders();
//        responseHeaders.set("Authorization",
//                "Bearer " + jwt);
//        Cookie cookie = new Cookie("tokenIperform",jwt);
//
//        cookie.setMaxAge(7 * 24 * 60 * 60);
//        cookie.setSecure(true);
//        cookie.setHttpOnly(true);
//        cookie.setPath("/");
//        response.addCookie(cookie);
        ResponseCookie resCookie = ResponseCookie.from("token",jwt)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .domain("iperform.ikameglobal.com")
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();
        response.addHeader("Set-Cookie", resCookie.toString());
        response.setHeader("Access-Control-Expose-Headers", "*");
        return ResponseEntity
                .ok()
//                .headers(responseHeaders)
                .body(authenHrm);
    }
    @GetMapping(value = "/get-all-users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity
                .ok()
                .body(functionHelper.getAllUserFromHrm());
    }
    @GetMapping("/xx")
    public ResponseEntity<?> setCookie(HttpServletResponse response) {
        // create a cookie
        return ResponseEntity
                .ok()
                .body(functionHelper.getMyTeamInfo("786acdc6-8a6b-4a93-9d21-286cfd65e4fa"));
    }
}
