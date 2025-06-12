package fun.dashspace.carrentalsystem.mail;

public interface EmailService {
    void sendRegistrationOtp(String email, String otpCode);
    void sendPasswordResetOtp(String email, String otpCode);

    void sendSimpleEmail(String to, String subject, String content);
    void sendHtmlEmail(String to, String subject, String content);
}
