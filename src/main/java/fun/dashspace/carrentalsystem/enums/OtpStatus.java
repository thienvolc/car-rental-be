package fun.dashspace.carrentalsystem.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OtpStatus {
    pending("pending"),
    verified("verified"),
    failed("failed"),
    expired("expired"),
    cancelled("cancelled");

    private final String status;
}
