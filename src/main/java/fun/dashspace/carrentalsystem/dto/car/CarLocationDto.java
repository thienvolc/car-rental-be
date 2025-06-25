package fun.dashspace.carrentalsystem.dto.car;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
