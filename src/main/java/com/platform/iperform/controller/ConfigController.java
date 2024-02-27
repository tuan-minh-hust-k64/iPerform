package com.platform.iperform.controller;

import com.platform.iperform.common.dto.request.ConfigRequest;
import com.platform.iperform.common.dto.response.ConfigResponse;
import com.platform.iperform.service.ConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api/config")


public class ConfigController {
    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }
    @PostMapping
    public ResponseEntity<ConfigResponse> createConfig(@RequestBody ConfigRequest configRequest) {
        ConfigResponse result = configService.createConfig(configRequest);
        return ResponseEntity.ok(result);
    }
    @PutMapping
    public ResponseEntity<ConfigResponse> updateConfig(@RequestBody ConfigRequest configRequest) {
        ConfigResponse result = configService.updateConfig(configRequest);
        return ResponseEntity.ok(result);
    }
    @GetMapping
    public ResponseEntity<ConfigResponse> getConfig() {
        ConfigResponse result = configService.getConfig();
        return ResponseEntity.ok(result);
    }
}
