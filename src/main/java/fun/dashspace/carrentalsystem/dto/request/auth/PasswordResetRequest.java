package fun.dashspace.carrentalsystem.dto.request.auth;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String email;
}
