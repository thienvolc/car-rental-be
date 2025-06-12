package fun.dashspace.carrentalsystem.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FuelType {
    gasoline("gasoline"),
    diesel("diesel"),
    electric("electric"),
    hydrid("hybrid");

    private final String fuelType;
}
