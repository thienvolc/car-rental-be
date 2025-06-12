package fun.dashspace.carrentalsystem.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface VerifyOtpRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email = "";
    String otpCode = "";

    String getEmail();
    String getOtpCode();
}
