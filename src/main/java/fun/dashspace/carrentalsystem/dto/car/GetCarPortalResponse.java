package fun.dashspace.carrentalsystem.dto.car;

import fun.dashspace.carrentalsystem.enums.ApprovalStatus;
import fun.dashspace.carrentalsystem.enums.CarStatus;
import fun.dashspace.carrentalsystem.enums.FuelType;
import fun.dashspace.carrentalsystem.enums.TransmissionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class GetCarResponse {
    private Integer carId;
    private Integer ownerId;

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
    private CarStatus status;
    private ApprovalStatus approvalStatus;

    private CarCertificateDto certificate;

    private List<CarImageDto> images;
}