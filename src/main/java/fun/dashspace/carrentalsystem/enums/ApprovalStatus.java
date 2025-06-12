package fun.dashspace.carrentalsystem.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApprovalStatus {
    pending("pending"),
    approval("approved"),
    rejected("rejected");

    private final String status;
}
