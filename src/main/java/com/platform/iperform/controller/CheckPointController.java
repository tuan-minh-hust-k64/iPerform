package com.platform.iperform.controller;

import com.platform.iperform.common.dto.CheckPointRequest;
import com.platform.iperform.common.dto.CheckPointResponse;
import com.platform.iperform.common.dto.KeyStepRequest;
import com.platform.iperform.common.dto.KeyStepResponse;
import com.platform.iperform.service.CheckPointService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/check-point")
public class CheckPointController {
    private final CheckPointService checkPointService;

    public CheckPointController(CheckPointService checkPointService) {
        this.checkPointService = checkPointService;
    }
    @PostMapping(value = "/get-by-user-id")
    public ResponseEntity<CheckPointResponse> getCheckPointByUserId(@RequestBody CheckPointRequest checkPointRequest) {
        CheckPointResponse result = checkPointService.getCheckPointByUserId(checkPointRequest);
        return ResponseEntity.ok(result);
    }
    @PostMapping(value = "/create")
    public ResponseEntity<CheckPointResponse> createCheckPoint(@RequestBody CheckPointRequest checkPointRequest) {
        CheckPointResponse result = checkPointService.createCheckPoint(checkPointRequest);
        return ResponseEntity.ok(result);
    }
    @PostMapping(value = "/update")
    public ResponseEntity<CheckPointResponse> updateEks(@RequestBody CheckPointRequest checkPointRequest) {
        CheckPointResponse result = checkPointService.updateCheckPoint(checkPointRequest);
        return ResponseEntity.ok(result);
    }
}
