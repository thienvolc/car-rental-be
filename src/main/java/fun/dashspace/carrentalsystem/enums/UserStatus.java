package fun.dashspace.carrentalsystem.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {
    active("active"),
    inactive("inactive"),
    banned("banned");

    private final String status;
}
