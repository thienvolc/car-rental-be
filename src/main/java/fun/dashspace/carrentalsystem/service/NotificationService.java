package fun.dashspace.carrentalsystem.service;

public interface NotificationService {
    void sendRegistrationOtp(String email, String otpCode);
    void sendPasswordResetOtp(String email, String otpCode);
}
