package fun.dashspace.carrentalsystem.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TripStatus {
    pending("pending"),
    approved("approved"),
    rejected("rejected"),
    in_progress("in_progress"),
    completed("completed"),
    cancelled("cancelled");

    private final String status;
}
