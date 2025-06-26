package fun.dashspace.carrentalsystem.dto.car;

import fun.dashspace.carrentalsystem.enums.FuelType;
import fun.dashspace.carrentalsystem.enums.TransmissionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CarResponseDTO {
    private Integer id;
    private String brand;
    private String model;
    private Integer numberOfSeats;
    private FuelType fuelType;
    private TransmissionType transmissionType;
    private BigDecimal pricePerDay;
    private BigDecimal totalPrice;
    private String licensePlateNumber;
    private Integer yearOfManufacture;
    private String description;
    private BigDecimal fuelConsumption;
    private CarLocationDto location;
    private List<CarImageDto> imageUrls;
    private String ownerName;
}