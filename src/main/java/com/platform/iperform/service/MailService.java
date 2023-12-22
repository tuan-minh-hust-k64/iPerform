package com.platform.iperform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailService {
    @Autowired
    private JavaMailSender mailSender;
    public void sendEmail() {
//        SimpleMailMessage message = new SimpleMailMessage();
//
//        message.setFrom(from);
//        message.setTo(checkPointRequest.getReceiver());
//        message.setSubject("[iKame-iPerform] Thông báo có request check-in từ Nguyễn Văn A");
//        message.setText("Xin chào ABC,\n" +
//                "Bạn vừa nhận được yêu cầu check-in từ Nguyễn Văn A.\n" +
//                "Click vào ĐÂY để gửi xem phần Check-in nháp và để lại comment cho Nguyễn Văn A. (link ĐÂY với phần check-in nháp)\n" +
//                "Click vào ĐÂY để hẹn lịch ngồi 1:1 với Nguyễn Văn A. (link ĐÂY với Google calendar https://calendar.google.com/calendar/u/0/r)\n" +
//                "Tìm hiểu thêm về quy trình Check-in EKS tại ĐÂY. (link ĐÂY với iWiki https://iwiki.ikameglobal.com/group/95ea3f10661f5fa5f80675d4)\n" +
//                "Nếu có vấn đề gì trong quá trình sử dụng, xin hãy để lại feedback tại ĐÂY (link ĐÂY với form feedback) hoặc liên hệ nguyetnt@ikameglobal.com để được sự hỗ trợ kịp thời! Cảm ơn bạn vì sự hợp tác này! (edited) ");
//
//        mailSender.send(message);
    }
}
