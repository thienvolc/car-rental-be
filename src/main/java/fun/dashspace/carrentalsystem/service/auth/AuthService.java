package fun.dashspace.carrentalsystem.service.auth;

import fun.dashspace.carrentalsystem.dto.request.auth.*;
import fun.dashspace.carrentalsystem.dto.response.auth.LoginResponse;
import fun.dashspace.carrentalsystem.dto.response.auth.RefreshTokenResponse;
import fun.dashspace.carrentalsystem.dto.response.auth.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest req);

    LoginResponse login(LoginResquest req);

    RefreshTokenResponse refreshToken(RefreshTokenRequest req);

    void logout(String refreshToken);

    void logoutAll(Integer userId);

    void verifyEmail(VerifyEmailRequest req);

    void requestPasswordReset(PasswordResetRequest req);

    void resetPassword(ResetPasswordRequest req);
}
