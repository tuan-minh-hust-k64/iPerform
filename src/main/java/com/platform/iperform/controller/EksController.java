package com.platform.iperform.controller;

import com.platform.iperform.common.dto.EksRequest;
import com.platform.iperform.common.dto.EksResponse;
import com.platform.iperform.service.EksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequestMapping(value = "/eks")
public class EksController {
    private final EksService eksService;

    public EksController(EksService eksService) {
        this.eksService = eksService;
    }

    @PostMapping
    public ResponseEntity<EksResponse> getEksByUserId(@RequestBody EksRequest eksRequest) {
        EksResponse result = eksService.getEksByUserId(eksRequest.getUserId(), eksRequest.getTimePeriod());
        return ResponseEntity.ok(result);
    }
    @PostMapping(value = "/create")
    public ResponseEntity<EksResponse> createEks(@RequestBody EksRequest eksRequest) {
        EksResponse result = eksService.createEks(eksRequest.getEks());
        return ResponseEntity.ok(result);
    }
    @GetMapping
    public ResponseEntity<String> testApi1() {
        return ResponseEntity.ok("ok");
    }
}
