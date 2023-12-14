package com.platform.iperform.controller;

import com.platform.iperform.common.dto.request.CheckPointRequest;
import com.platform.iperform.common.dto.response.CheckPointResponse;
import com.platform.iperform.common.exception.AuthenticateException;
import com.platform.iperform.common.exception.NotFoundException;
import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.service.CheckPointService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@CrossOrigin(origins = {"http://localhost:3000", "https://iperform.ikameglobal.com"}, allowCredentials = "true")

@RequestMapping(value = "/api/check-point")
public class CheckPointController {
    private final CheckPointService checkPointService;
    private final FunctionHelper functionHelper;

    public CheckPointController(CheckPointService checkPointService, FunctionHelper functionHelper) {
        this.checkPointService = checkPointService;
        this.functionHelper = functionHelper;
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<CheckPointResponse> getCheckPointByIdAndUserId(@PathVariable UUID id) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        CheckPointResponse result = checkPointService.findById(id);
        if(functionHelper.checkPermissionHrm(UUID.fromString(userId), result.getData().getUserId())) {
            return ResponseEntity.ok(result);
        } else {
            throw new AuthenticateException("You are not permission!");
        }

    }
    @GetMapping
    public ResponseEntity<CheckPointResponse> getCheckPointByUserId(@RequestParam UUID userId) {
        CheckPointResponse result = checkPointService.getCheckPointByUserId(CheckPointRequest.builder()
                .userId(functionHelper.authorizationMiddleware(
                        UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName()),
                        userId
                ))
                .build());
        return ResponseEntity.ok(result);
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
        CheckPointResponse result = checkPointService.updateCheckPointByUserId(
                checkPointRequest,
                functionHelper.authorizationMiddleware(UUID.fromString(userId), checkPointRequest.getCheckPoint().getUserId()));
        return ResponseEntity.ok(result);
    }
}
