package fun.dashspace.carrentalsystem.dto.request.auth;

import lombok.Data;

@Data
public class ResetPasswordRequest implements VerifyOtpRequest {
    private String email;
    private String newPassword;
    private String otpCode;
}
