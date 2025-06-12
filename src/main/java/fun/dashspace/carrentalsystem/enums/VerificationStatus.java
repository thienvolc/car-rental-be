package fun.dashspace.carrentalsystem.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VerificationStatus {
    pending("pending"),
    verified("verified"),
    rejected("rejected");

    private final String status;
}
