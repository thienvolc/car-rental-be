package fun.dashspace.carrentalsystem.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OtpRequestType {
    registration("registration"),
    forgot_password("forgot_password"),
    host_registration("host_registration");

    private final String requestType;
}
