package com.platform.iperform.controller;

import com.platform.iperform.common.dto.request.EksRequest;
import com.platform.iperform.common.dto.response.EksResponse;
import com.platform.iperform.common.exception.AuthenticateException;
import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.libs.hrms_provider.HrmsProvider;
import com.platform.iperform.libs.hrms_provider.HrmsV3;
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
    private final HrmsProvider hrmsProvider;
    public EksController(EksService eksService,
                         FunctionHelper functionHelper,
                         HrmsV3 hrmsV3
    ) {
        this.eksService = eksService;
        this.functionHelper = functionHelper;
        this.hrmsProvider = hrmsV3;
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
        if(hrmsProvider.checkPermissionHrm(UUID.fromString(userId), result.getData().getUserId())) {
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
