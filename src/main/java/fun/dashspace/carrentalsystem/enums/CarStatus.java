package fun.dashspace.carrentalsystem.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CarStatus {
    active("active"),
    rented("rented"),
    inactive("inactive"),
    banned("banned");

    private final String status;
}
