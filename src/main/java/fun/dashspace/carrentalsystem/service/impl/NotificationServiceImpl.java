package fun.dashspace.carrentalsystem.service.impl;

import fun.dashspace.carrentalsystem.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final JavaMailSender mailSender;

    @Override
    public void sendRegistrationOtp(String email, String otpCode) {
        String subject = "DashSpace - Registration OTP";
        String content = String.format("""
                Welcome to DashSpace!
                
                Your registration OTP code is: %s
                
                This code will expire in 5 minutes.
                
                If you didn't request this, please ignore this email.
                
                Best regards,
                DashSpace Team
                """, otpCode);

        sendSimpleEmail(email, subject, content);
    }

    @Override
    public void sendPasswordResetOtp(String email, String otpCode) {
        String subject = "DashSpace - Password Reset OTP";
        String content = String.format("""
                Password Reset Request
                
                Your password reset OTP code is: %s
                
                This code will expire in 5 minutes.
                
                If you didn't request this, please ignore this email.
                
                Best regards,
                DashSpace Team
                """, otpCode);

        sendSimpleEmail(email, subject, content);
    }

    private void sendSimpleEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }
}