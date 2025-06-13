package fun.dashspace.carrentalsystem.dto.response.car;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarRegistrationResponse {
    private Integer carId;
    private String licensePlateNumber;
    private String brand;
    private String model;
    private String message;
}