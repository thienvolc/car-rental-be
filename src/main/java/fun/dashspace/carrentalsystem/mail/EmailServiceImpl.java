package fun.dashspace.carrentalsystem.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

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
        log.info("Registration OTP sent to: {}", email);
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
        log.info("Password reset OTP sent to: {}", email);
    }

    @Override
    public void sendSimpleEmail(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
            log.debug("Simple email sent to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to: {}", to, e);
        }
    }

    @Override
    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(message);
            log.debug("HTML email sent to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send HTML email to: {}", to, e);
        }
    }
}