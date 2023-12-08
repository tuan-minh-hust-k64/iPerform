package com.platform.iperform.controller;

import com.platform.iperform.common.dto.request.CheckPointRequest;
import com.platform.iperform.common.dto.response.CheckPointResponse;
import com.platform.iperform.service.CheckPointService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")

@RequestMapping(value = "/check-point")
public class CheckPointController {
    private final CheckPointService checkPointService;

    public CheckPointController(CheckPointService checkPointService) {
        this.checkPointService = checkPointService;
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<CheckPointResponse> getCheckPointByIdAndUserId(@PathVariable UUID id) {
        CheckPointResponse result = checkPointService.findById(id);
        return ResponseEntity.ok(result);
    }
    @GetMapping
    public ResponseEntity<CheckPointResponse> getCheckPointByUserId(@RequestParam UUID userId) {
        CheckPointResponse result = checkPointService.getCheckPointByUserId(CheckPointRequest.builder()
                        .userId(userId)
                .build());
        return ResponseEntity.ok(result);
    }
    @PostMapping
    public ResponseEntity<CheckPointResponse> createCheckPoint(@RequestBody CheckPointRequest checkPointRequest) {
        CheckPointResponse result = checkPointService.createCheckPoint(checkPointRequest);
        return ResponseEntity.ok(result);
    }
    @PutMapping
    public ResponseEntity<CheckPointResponse> updateCheckPoint(@RequestBody CheckPointRequest checkPointRequest) {
        CheckPointResponse result = checkPointService.updateCheckPoint(checkPointRequest);
        return ResponseEntity.ok(result);
    }
}
