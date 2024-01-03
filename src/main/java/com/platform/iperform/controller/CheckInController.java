package com.platform.iperform.controller;

import com.platform.iperform.common.dto.request.CheckInRequest;
import com.platform.iperform.common.dto.request.EksRequest;
import com.platform.iperform.common.dto.response.CheckInResponse;
import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.model.Eks;
import com.platform.iperform.service.CheckInService;
import com.platform.iperform.service.EksService;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
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

@RequestMapping(value = "/api/check-in")
public class CheckInController {
    @Autowired
    private JavaMailSender mailSender;
    private final CheckInService checkInService;
    private final EksService eksService;
    private final FunctionHelper functionHelper;

    public CheckInController(JavaMailSender mailSender, CheckInService checkInService, EksService eksService, FunctionHelper functionHelper) {
        this.mailSender = mailSender;
        this.checkInService = checkInService;
        this.eksService = eksService;
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
                message.setSubject("[iKame-iPerform] Thông báo có request check-in từ " + fromName);
                message.setContent(
                        "Xin chào <strong>" + item.get("name") + "</strong>,\n" +
                                "<br/>\n" +
                                "Bạn vừa nhận được yêu cầu <strong>CHECK-IN</strong> từ " + fromName + ".\n" +
                                " <br/>\n" +
                                " Click vào <a href=\"https://calendar.google.com/calendar/u/0/r\">ĐÂY</a> để hẹn lịch ngồi 1:1 với " + fromName + ".\n" +
                                "<br/>\n" +
                                "Tìm hiểu thêm về quy trình Check-in tại <a href=\"https://iwiki.ikameglobal.com/group/95ea3f10661f5fa5f80675d4\">ĐÂY</a>.\n" +
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
