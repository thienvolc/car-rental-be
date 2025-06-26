package fun.dashspace.carrentalsystem.dto.car;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateCarRentalInfoRequest {
    private BigDecimal basePricePerDay;
    private String description;
}
