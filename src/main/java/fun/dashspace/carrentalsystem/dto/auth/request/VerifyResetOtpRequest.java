package fun.dashspace.carrentalsystem.dto.auth.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyResetOtpRequest {
    private String email;
    private String otpCode;
}
