package com.platform.iperform.controller;

import com.platform.iperform.common.dto.CheckInRequest;
import com.platform.iperform.common.dto.CheckInResponse;
import com.platform.iperform.common.dto.KeyStepRequest;
import com.platform.iperform.common.dto.KeyStepResponse;
import com.platform.iperform.service.CheckInService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/check-in")
public class CheckInController {
    private final CheckInService checkInService;

    public CheckInController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }
    @PostMapping(value = "/create")
    public ResponseEntity<CheckInResponse> createCheckIn(@RequestBody CheckInRequest checkInRequest) {
        CheckInResponse result = checkInService.createCheckIn(checkInRequest);
        return ResponseEntity.ok(result);
    }
    @PostMapping(value = "/update")
    public ResponseEntity<CheckInResponse> updateCheckIn(@RequestBody CheckInRequest checkInRequest) {
        CheckInResponse result = checkInService.updateCheckIn(checkInRequest);
        return ResponseEntity.ok(result);
    }
}
