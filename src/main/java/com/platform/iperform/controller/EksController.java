package com.platform.iperform.controller;

import com.platform.iperform.common.dto.request.EksRequest;
import com.platform.iperform.common.dto.response.EksResponse;
import com.platform.iperform.service.EksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping
    public ResponseEntity<EksResponse> getEksByUserId(@RequestParam UUID userId, @RequestParam String timePeriod) {
        EksResponse result = eksService.getEksByUserId(userId, timePeriod);
        return ResponseEntity.ok(result);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<EksResponse> getEksById(@PathVariable UUID id) {
        EksResponse result = eksService.getEksById(id);
        return ResponseEntity.ok(result);
    }
    @PostMapping
    public ResponseEntity<EksResponse> createEks(@RequestBody EksRequest eksRequest) {

        EksResponse result = eksService.createEks(eksRequest.getEks());
        return ResponseEntity.ok(result);
    }
    @PutMapping
    public ResponseEntity<EksResponse> updateEks(@RequestBody EksRequest eksRequest) {
        EksResponse result = eksService.updateEks(eksRequest);
        return ResponseEntity.ok(result);
    }
}
