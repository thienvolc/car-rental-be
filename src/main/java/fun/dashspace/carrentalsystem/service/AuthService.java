package fun.dashspace.carrentalsystem.service;

import fun.dashspace.carrentalsystem.dto.auth.request.*;
import fun.dashspace.carrentalsystem.dto.auth.response.LoginResponse;
import fun.dashspace.carrentalsystem.dto.auth.response.RefreshTokenResponse;
import fun.dashspace.carrentalsystem.security.CustomUserDetails;

public interface AuthService {
    void sendRegistrationEmailOtp(RegistrationEmailRequest req);

    void verifyRegistraionEmailOtp(VerifyOtpRequest req);

    void registerRenter(RenterRegistrationRequest req);
    LoginResponse login(LoginResquest req);

    RefreshTokenResponse refreshToken(String refreshToken, CustomUserDetails userDetails);

    void logout(Integer userId, String refreshToken);
    void logoutAll(Integer userId);

    void resendOtp(ResendOtpRequest req);

    void forgotPassword(String email);

    void verifyResetOtp(VerifyResetOtpRequest req);

    void resetPassword(ResetPasswordRequest req);
}
