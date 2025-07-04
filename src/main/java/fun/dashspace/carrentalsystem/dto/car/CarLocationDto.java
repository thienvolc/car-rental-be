package fun.dashspace.carrentalsystem.dto.car;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class CarLocationDto {
    private String province;
    private String district;
    private String ward;
    private String addressDetails;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
