package fun.dashspace.carrentalsystem.dto.auth.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
    private String email;
    private String otpCode;
    private String newPassword;
}
