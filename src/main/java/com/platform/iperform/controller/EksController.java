package com.platform.iperform.controller;

import com.platform.iperform.common.dto.request.EksRequest;
import com.platform.iperform.common.dto.response.EksResponse;
import com.platform.iperform.common.exception.NotFoundException;
import com.platform.iperform.service.EksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
        if(userId.equals(UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName()))) {
            EksResponse result = eksService.getEksByUserId(userId, timePeriod);
            return ResponseEntity.ok(result);
        } else {
            throw new NotFoundException("Not found resource!!!");
        }

    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<EksResponse> getEksByIdAndUserId(@PathVariable UUID id) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        EksResponse result = eksService.getEksByIdAndUserId(id, UUID.fromString(userId));
        return ResponseEntity.ok(result);
    }
    @PostMapping
    public ResponseEntity<EksResponse> createEks(@RequestBody EksRequest eksRequest) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        eksRequest.getEks().forEach(item -> item.setUserId(UUID.fromString(userId)));
        EksResponse result = eksService.createEks(eksRequest.getEks());
        return ResponseEntity.ok(result);
    }
    @PutMapping
    public ResponseEntity<EksResponse> updateEks(@RequestBody EksRequest eksRequest) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        EksResponse result = eksService.updateEksByIdAndUserId(eksRequest, UUID.fromString(userId));
        return ResponseEntity.ok(result);
    }
}
