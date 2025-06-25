package fun.dashspace.carrentalsystem.dto.car;

import fun.dashspace.carrentalsystem.enums.ApprovalStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewCarRequest {
    private ApprovalStatus status;
}
