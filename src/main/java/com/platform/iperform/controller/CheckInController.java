package com.platform.iperform.controller;

import com.platform.iperform.common.dto.request.CheckInRequest;
import com.platform.iperform.common.dto.response.CheckInResponse;
import com.platform.iperform.service.CheckInService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = {"http://localhost:3000", "https://iperform.ikameglobal.com"}, allowCredentials = "true")

@RequestMapping(value = "/api/check-in")
public class CheckInController {
    private final CheckInService checkInService;

    public CheckInController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }
    @PostMapping
    public ResponseEntity<CheckInResponse> createCheckIn(@RequestBody CheckInRequest checkInRequest) {
        CheckInResponse result = checkInService.createCheckIn(checkInRequest);
        return ResponseEntity.ok(result);
    }

    @PutMapping
    public ResponseEntity<CheckInResponse> updateCheckIn(@RequestBody CheckInRequest checkInRequest) {
        CheckInResponse result = checkInService.updateCheckIn(checkInRequest);
        return ResponseEntity.ok(result);
    }
}
