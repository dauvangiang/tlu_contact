package com.mobile.group.tlu_contact_be.service;


import com.google.firebase.auth.ActionCodeSettings;
import com.mobile.group.tlu_contact_be.exceptions.CustomException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;


    @Async
    public void sendCustomEmail(String userEmail,String link) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(userEmail);
            helper.setSubject("Xác nhận tài khoản của bạn");
            helper.setText(buildEmailContent(link), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new CustomException("Không thể gửi email: " + e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    private String buildEmailContent(String link) {
        return "<h2>Chào bạn,</h2>" +
                "<p>Cảm ơn bạn đã đăng ký tài khoản trên hệ thống TLU Contact.</p>" +
                "<p>Vui lòng nhấn vào link bên dưới để xác thực email của bạn và hoàn tất đăng ký:</p>" +
                "<a href='" + link + "' style='padding:10px; background-color:#008CBA; color:white; text-decoration:none;'>Xác thực tài khoản</a>" +
                "<p>Nếu bạn không thực hiện đăng ký, vui lòng bỏ qua email này.</p>" +
                "<p>Trân trọng,<br>TLU Contact</p>";
    }
}
