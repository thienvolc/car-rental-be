package fun.dashspace.carrentalsystem.dto.request.car;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CarRegistrationRequestDTO {

    @NotBlank(message = "License plate number is required")
    @Size(max = 20, message = "License plate number must not exceed 20 characters")
    private String licensePlateNumber;

    @NotNull(message = "Year of manufacture is required")
    @Min(value = 1900, message = "Year of manufacture must be after 1900")
    @Max(value = 2030, message = "Year of manufacture must be before 2030")
    private Integer yearOfManufacture;

    @NotBlank(message = "Brand is required")
    @Size(max = 50, message = "Brand must not exceed 50 characters")
    private String brand;

    @NotBlank(message = "Model is required")
    @Size(max = 100, message = "Model must not exceed 100 characters")
    private String model;

    @NotNull(message = "Number of seats is required")
    @Min(value = 2, message = "Number of seats must be at least 2")
    @Max(value = 16, message = "Number of seats must not exceed 16")
    private Integer numberOfSeats;

    @NotNull(message = "Fuel type is required")
    private String fuelType;

    @NotNull(message = "Transmission type is required")
    private String transmissionType;

    @DecimalMin(value = "0.0", message = "Fuel consumption must be positive")
    @DecimalMax(value = "99.99", message = "Fuel consumption must not exceed 99.99")
    private BigDecimal fuelConsumption;

    @NotNull(message = "Base price per day is required")
    @DecimalMin(value = "0.0", message = "Base price must be positive")
    private BigDecimal basePricePerDay;

    private String description;

    @NotNull(message = "Location information is required")
    private CarLocationDTO location;
}