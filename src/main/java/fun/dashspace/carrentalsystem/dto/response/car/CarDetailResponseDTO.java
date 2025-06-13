package fun.dashspace.carrentalsystem.dto.response.car;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class CarDetailResponseDTO {
    private Integer id;
    private String brand;
    private String model;
    private String licensePlateNumber;
    private Integer yearOfManufacture;
    private Integer numberOfSeats;
    private String fuelType;
    private String transmissionType;
    private BigDecimal fuelConsumption;
    private BigDecimal basePricePerDay;
    private String description;
    private List<CarImageDTO> images;
    private LocationDTO location;
    private CarOwnerDTO owner;
}