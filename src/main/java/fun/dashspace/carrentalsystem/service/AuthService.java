package fun.dashspace.carrentalsystem.service;

import fun.dashspace.carrentalsystem.dto.HostRegistrationEmailRequest;
import fun.dashspace.carrentalsystem.dto.auth.HostRegistrationRequest;
import fun.dashspace.carrentalsystem.dto.auth.request.*;
import fun.dashspace.carrentalsystem.dto.auth.response.LoginResponse;
import fun.dashspace.carrentalsystem.dto.auth.response.RefreshTokenResponse;

public interface AuthService {
    void sendRegistrationEmailOtp(RegistrationEmailRequest req);

    void verifyRegistraionEmailOtp(VerifyOtpRequest req);

    void registerRenter(RenterRegistrationRequest req);
    LoginResponse login(LoginResquest req);

    RefreshTokenResponse refreshToken(String refreshToken);

    void logout(Integer userId, String refreshToken);
    void logoutAll(Integer userId);

    void resendOtp(ResendOtpRequest req);

    void forgotPassword(String email);

    void verifyResetOtp(VerifyOtpRequest req);

    void resetPassword(ResetPasswordRequest req);

    void registerHost(HostRegistrationRequest req);

    void sendHostRegistrationEmailOtp(HostRegistrationEmailRequest req);

    void verifyHostRegistraionEmailOtp(VerifyOtpRequest req);
}
