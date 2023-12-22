package com.platform.iperform.controller;

import com.platform.iperform.common.dto.request.CheckInRequest;
import com.platform.iperform.common.dto.request.CheckPointRequest;
import com.platform.iperform.common.dto.response.CheckInResponse;
import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.service.CheckInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin(origins = {"http://localhost:3000", "https://iperform.ikameglobal.com"}, allowCredentials = "true")

@RequestMapping(value = "/api/check-in")
public class CheckInController {
    @Autowired
    private JavaMailSender mailSender;
    private final CheckInService checkInService;
    private final FunctionHelper functionHelper;

    public CheckInController(CheckInService checkInService, FunctionHelper functionHelper) {
        this.checkInService = checkInService;
        this.functionHelper = functionHelper;
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
    @PostMapping(value = "/request-check-in")
    public ResponseEntity<String> requestCheckIn() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            Map<String, Object> managers = functionHelper.getManagerInfo(userId);
            String fromName = (String) managers.get("email");
            List<Map<String, String>> managerInfo= (List<Map<String, String>>) managers.get("managers");
            managerInfo.forEach(item -> {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(fromName);
                message.setTo(item.get("email"));
                message.setSubject("[iKame-iPerform] Thông báo có request check-point từ " + fromName);
                message.setText("Xin chào ABC,\n" +
                        "Bạn vừa nhận được yêu cầu check-in từ " + fromName + ".\n" +
                        "Click vào ĐÂY để hẹn lịch ngồi 1:1. (link ĐÂY với Google calendar https://calendar.google.com/calendar/u/0/r)\n" +
                        "Tìm hiểu thêm về quy trình Check-in EKS tại ĐÂY. (link ĐÂY với iWiki https://iwiki.ikameglobal.com/group/95ea3f10661f5fa5f80675d4)\n" +
                        "Nếu có vấn đề gì trong quá trình sử dụng, xin hãy để lại feedback tại ĐÂY (link ĐÂY với form feedback) hoặc liên hệ nguyetnt@ikameglobal.com để được sự hỗ trợ kịp thời! Cảm ơn bạn vì sự hợp tác này! (edited) ");

                mailSender.send(message);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok("OK");
    }
}
