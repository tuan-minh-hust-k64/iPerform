package com.platform.iperform.controller;

import com.platform.iperform.common.dto.request.CheckPointRequest;
import com.platform.iperform.common.dto.response.CheckPointResponse;
import com.platform.iperform.common.exception.NotFoundException;
import com.platform.iperform.service.CheckPointService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping(value = "/check-point")
public class CheckPointController {
    private final CheckPointService checkPointService;

    public CheckPointController(CheckPointService checkPointService) {
        this.checkPointService = checkPointService;
    }
    @GetMapping
    public ResponseEntity<CheckPointResponse> getCheckPointByUserId(@RequestParam UUID userId) {
        if(userId.equals(UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName()))) {
            CheckPointResponse result = checkPointService.getCheckPointByUserId(CheckPointRequest.builder()
                    .userId(userId)
                    .build());
            return ResponseEntity.ok(result);
        } else {
            throw new NotFoundException("Not found resource!!!");
        }

    }
    @PostMapping
    public ResponseEntity<CheckPointResponse> createCheckPoint(@RequestBody CheckPointRequest checkPointRequest) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        checkPointRequest.getCheckPoint().setUserId(UUID.fromString(userId));
        CheckPointResponse result = checkPointService.createCheckPoint(checkPointRequest);
        return ResponseEntity.ok(result);
    }
    @PutMapping
    public ResponseEntity<CheckPointResponse> updateCheckPoint(@RequestBody CheckPointRequest checkPointRequest) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        CheckPointResponse result = checkPointService.updateCheckPointByUserId(checkPointRequest, UUID.fromString(userId));
        return ResponseEntity.ok(result);
    }
}
