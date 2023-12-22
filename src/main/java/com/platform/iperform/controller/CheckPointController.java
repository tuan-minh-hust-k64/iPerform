package com.platform.iperform.controller;

import com.platform.iperform.common.dto.request.CheckPointRequest;
import com.platform.iperform.common.dto.response.CheckPointResponse;
import com.platform.iperform.common.exception.AuthenticateException;
import com.platform.iperform.common.exception.NotFoundException;
import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.common.valueobject.CheckPointStatus;
import com.platform.iperform.model.CheckPoint;
import com.platform.iperform.service.CheckPointService;
import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

@Controller
@CrossOrigin(origins = {"http://localhost:3000", "https://iperform.ikameglobal.com"}, allowCredentials = "true")
@Slf4j
@RequestMapping(value = "/api/check-point")
public class CheckPointController {
    private final CheckPointService checkPointService;
    private final FunctionHelper functionHelper;
    @Autowired
    private JavaMailSender mailSender;

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
    @PostMapping(value = "/request-checkpoint")
    public ResponseEntity<String> requestCheckPoint(@RequestBody CheckPointRequest checkPointRequest) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        CheckPointResponse result = checkPointService.updateCheckPointByUserId(
                CheckPointRequest.builder()
                        .checkPoint(CheckPoint.builder()
                                .id(checkPointRequest.getCheckPointId())
                                .status(CheckPointStatus.PENDING)
                                .checkPointItems(List.of())
                                .build())
                        .build(),
                functionHelper.authorizationMiddleware(UUID.fromString(userId), UUID.fromString(userId)));
        try {
            Map<String, Object> managers = functionHelper.getManagerInfo(userId);
            String fromName = (String) managers.get("name");
            String fromEmail = (String) managers.get("email");
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            Session session = Session.getInstance(props,
                    new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication("especiallygamesbdd@gmail.com", "wiwvcqsubgnrzrwe");
                        }
                    });
            List<Map<String, String>> managerInfo= (List<Map<String, String>>) managers.get("managers");
            for (Map<String, String> item : managerInfo) {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(fromEmail));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(item.get("email")));
                message.setSubject("[iKame-iPerform] Thông báo có request check-point từ " + fromName);
                message.setContent(
                        "Xin chào <strong>" + item.get("name") + "</strong>,\n" +
                        "<br/>\n" +
                        "Bạn vừa nhận được yêu cầu <strong>CHECK-POINT</strong> từ " + fromName + ".\n" +
                        " <br/>\n" +
                        " Click vào <a href=\"https://calendar.google.com/calendar/u/0/r\">ĐÂY</a> để hẹn lịch ngồi 1:1 với " + fromName + ".\n" +
                        "<br/>\n" +
                        "Tìm hiểu thêm về quy trình Check-point tại <a href=\"https://iwiki.ikameglobal.com/doc/a3bbdcda27b04b522bf6ca66\">ĐÂY</a>.\n" +
                        "<br/>\n" +
                        "Nếu có vấn đề gì trong quá trình sử dụng, xin hãy để lại feedback tại <a href=\"https://form.asana.com/?k=KICYSRCeDFDjeD1p8y317g&d=1204141578322999\">ĐÂY</a> hoặc liên hệ nguyetnt@ikameglobal.com để được sự hỗ trợ kịp thời! Cảm ơn bạn vì sự hợp tác này!",
                        "text/html;charset=UTF-8");
                Transport.send(message);
            }
        } catch (IOException | MessagingException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok("OK");
    }


}
