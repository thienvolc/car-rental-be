package fun.dashspace.carrentalsystem.service;

import fun.dashspace.carrentalsystem.enums.OtpRequestType;

public interface OtpRequestService {
    void sendRegistrationOtp(String email);
    void verifyRegistrationOtp(String email, String otp);

    void resendOtp(String email, OtpRequestType type);

    void validateRegistrationEmailVerified(String email);

    void cleanupExpiredOtpRequests();

    void sendForgotPasswordOtp(String email);

    void verifyForgotPasswordOtp(String email, String otp);

    void validateForgotPasswordVerified(String email);
}
