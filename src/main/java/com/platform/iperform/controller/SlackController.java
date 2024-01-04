package com.platform.iperform.controller;

import com.platform.iperform.service.SlackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping(value = "/api/slack")
@CrossOrigin(origins = {"http://localhost:3000", "https://iperform.ikameglobal.com"}, allowCredentials = "true")
public class SlackController {
    private final SlackService slackService;

    public SlackController(SlackService slackService) {
        this.slackService = slackService;
    }
    @GetMapping
    public ResponseEntity<?> sendSlackDM() {
        return ResponseEntity.ok("OK");
    }
}
