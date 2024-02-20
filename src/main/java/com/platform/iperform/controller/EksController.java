package com.platform.iperform.controller;

import com.platform.iperform.common.dto.request.EksRequest;
import com.platform.iperform.common.dto.response.EksResponse;
import com.platform.iperform.common.exception.AuthenticateException;
import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.common.valueobject.Category;
import com.platform.iperform.service.EksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequestMapping(value = "/api/eks")
@CrossOrigin(origins = {"http://localhost:3000", "https://iperform.ikameglobal.com"}, allowCredentials = "true")

public class EksController {
    private final EksService eksService;
    public final FunctionHelper functionHelper;
    public EksController(EksService eksService, FunctionHelper functionHelper) {
        this.eksService = eksService;
        this.functionHelper = functionHelper;
    }

    @GetMapping
    public ResponseEntity<EksResponse> getEksByUserId(@RequestParam UUID userId, @RequestParam(required = false) String timePeriod, @RequestParam(required = false) String category) {
        EksResponse result = eksService.getEksByUserId(
                functionHelper.authorizationMiddleware(
                        UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName()),
                        userId),
                timePeriod,
                category);
        return ResponseEntity.ok(result);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<EksResponse> getEksByIdAndUserId(@PathVariable UUID id) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        EksResponse result = eksService.getEksById(id);
        if(functionHelper.checkPermissionHrm(UUID.fromString(userId), result.getData().getUserId())) {
            return ResponseEntity.ok(result);
        } else {
            throw new AuthenticateException("You are not permission");
        }

    }
    @PostMapping
    public ResponseEntity<EksResponse> createEks(@RequestBody EksRequest eksRequest) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        EksResponse result = eksService.createEks(eksRequest.getEks(), UUID.fromString(userId));
        return ResponseEntity.ok(result);
    }
    @PutMapping
    public ResponseEntity<EksResponse> updateEks(@RequestBody EksRequest eksRequest) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        EksResponse result = eksService.updateEksByIdAndUserId(
                eksRequest,
                functionHelper.authorizationMiddleware(UUID.fromString(userId), eksRequest.getData().getUserId())
                );
        return ResponseEntity.ok(result);
    }
}
