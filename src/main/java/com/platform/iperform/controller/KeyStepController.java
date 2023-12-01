package com.platform.iperform.controller;

import com.platform.iperform.common.dto.EksRequest;
import com.platform.iperform.common.dto.EksResponse;
import com.platform.iperform.common.dto.KeyStepRequest;
import com.platform.iperform.common.dto.KeyStepResponse;
import com.platform.iperform.service.KeyStepService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Controller
@RequestMapping(value = "/key-step")
public class KeyStepController {
    private final KeyStepService keyStepService;

    public KeyStepController(KeyStepService keyStepService) {
        this.keyStepService = keyStepService;
    }
    @PostMapping
    public ResponseEntity<KeyStepResponse> createKeyStep(@RequestBody KeyStepRequest keyStepRequest) {
        KeyStepResponse result = keyStepService.createKeyStep(keyStepRequest);
        return ResponseEntity.ok(result);
    }
    @PutMapping
    public ResponseEntity<KeyStepResponse> updateEks(@RequestBody KeyStepRequest keyStepRequest) {
        KeyStepResponse result = keyStepService.updateKeyStep(keyStepRequest);
        return ResponseEntity.ok(result);
    }
}
