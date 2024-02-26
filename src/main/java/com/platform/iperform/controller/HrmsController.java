package com.platform.iperform.controller;

import com.platform.iperform.common.dto.hrms.models.HrmsUser;
import com.platform.iperform.libs.hrms_provider.HrmsProvider;
import com.platform.iperform.libs.hrms_provider.HrmsV3;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hrms")
@CrossOrigin(origins = {"http://localhost:3000", "https://iperform.ikameglobal.com"}, allowCredentials = "true")
public class HrmsController {
    private final HrmsProvider hrmsProvider;

    public HrmsController(HrmsV3 hrmsV3) {
        this.hrmsProvider = hrmsV3;
    }

    @GetMapping("/users")
    public ResponseEntity<List<HrmsUser>> getAllUsers() throws Exception {
        return ResponseEntity.ok(hrmsProvider.getAllUsers());
    }
}
