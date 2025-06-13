package fun.dashspace.carrentalsystem.dto.request.car;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CarLocationDTO {

    @NotBlank(message = "Province is required")
    @Size(max = 100, message = "Province must not exceed 100 characters")
    private String province;

    @NotBlank(message = "District is required")
    @Size(max = 100, message = "District must not exceed 100 characters")
    private String district;

    @NotBlank(message = "Ward is required")
    @Size(max = 100, message = "Ward must not exceed 100 characters")
    private String ward;

    @NotBlank(message = "Address details are required")
    private String addressDetails;

    private BigDecimal latitude;
    private BigDecimal longitude;
}