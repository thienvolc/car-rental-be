package fun.dashspace.carrentalsystem.dto.response.car;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CarResponseDTO {
    private Integer id;
    private String brand;
    private String model;
    private Integer numberOfSeats;
    private String fuelType;
    private String transmissionType;
    private BigDecimal pricePerDay;
    private BigDecimal totalPrice;
    private String licensePlateNumber;
    private Integer yearOfManufacture;
    private String description;
    private BigDecimal fuelConsumption;
    private LocationDTO location;
    private List<CarImageDTO> imageUrls;
    private String ownerName;
}