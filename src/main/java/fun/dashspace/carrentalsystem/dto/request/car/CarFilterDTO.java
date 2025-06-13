package fun.dashspace.carrentalsystem.dto.request.car;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CarFilterDTO {
    private String brand;
    private Integer seats;
    private String fuelType;
    private String transmissionType;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}