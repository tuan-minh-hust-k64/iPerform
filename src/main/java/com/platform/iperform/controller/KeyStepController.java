package com.platform.iperform.controller;

import com.platform.iperform.common.dto.request.KeyStepRequest;
import com.platform.iperform.common.dto.response.KeyStepResponse;
import com.platform.iperform.service.KeyStepService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api/key-step")
@CrossOrigin(origins = {"http://localhost:3000", "https://iperform.ikameglobal.com"}, allowCredentials = "true")


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
