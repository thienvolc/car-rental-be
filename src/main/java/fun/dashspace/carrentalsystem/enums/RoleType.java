package fun.dashspace.carrentalsystem.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {
    admin("admin"),
    renter("renter"),
    host("host");

    private final String role;
}
