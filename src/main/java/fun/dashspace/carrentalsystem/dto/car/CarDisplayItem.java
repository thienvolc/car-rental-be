package fun.dashspace.carrentalsystem.dto.car;

import fun.dashspace.carrentalsystem.enums.ApprovalStatus;
import fun.dashspace.carrentalsystem.enums.CarStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class CarDisplayItem {
    private Integer carId;

    private Integer yearOfManufacture;
    private String brand;
    private String model;
    private BigDecimal basePricePerDay;
    private CarLocationDto location;
    private CarStatus status;
    private ApprovalStatus approvalStatus;
    private String mainImageUrl;
}
