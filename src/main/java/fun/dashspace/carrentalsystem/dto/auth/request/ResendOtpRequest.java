package fun.dashspace.carrentalsystem.dto.auth.request;

import fun.dashspace.carrentalsystem.enums.OtpRequestType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResendOtpRequest {
    private String email;
    private OtpRequestType type;
}
