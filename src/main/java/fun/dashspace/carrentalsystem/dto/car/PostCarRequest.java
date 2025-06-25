package fun.dashspace.carrentalsystem.dto.car;

import fun.dashspace.carrentalsystem.enums.FuelType;
import fun.dashspace.carrentalsystem.enums.TransmissionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PostCarRequest {
    private String licensePlateNumber;
    private Integer yearOfManufacture;
    private String brand;
    private String model;
    private Integer numberOfSeats;
    private FuelType fuelType;
    private TransmissionType transmissionType;
    private BigDecimal fuelConsumption;
    private BigDecimal basePricePerDay;
    private String description;
    private CarLocationDto location;
}
