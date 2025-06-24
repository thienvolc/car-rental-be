package fun.dashspace.carrentalsystem.entity;

import fun.dashspace.carrentalsystem.entity.base.BaseEntity;
import fun.dashspace.carrentalsystem.enums.ApprovalStatus;
import fun.dashspace.carrentalsystem.enums.CarStatus;
import fun.dashspace.carrentalsystem.enums.FuelType;
import fun.dashspace.carrentalsystem.enums.TransmissionType;
import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cars")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = {"owner", "location", "images", "certificate", "trips"})
@ToString(callSuper = true, exclude = {"owner", "location", "images", "certificate", "trips"})
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Car extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    @NotNull(message = "Owner is required")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    @NotNull(message = "Location is required")
    private CarLocation location;

    @Column(name = "license_plate_number", length = 20, nullable = false, unique = true)
    @NotBlank(message = "License plate number is required")
    @Size(max = 20, message = "License plate number must not exceed 20 characters")
    private String licensePlateNumber;

    @Column(name = "year_of_manufacture", nullable = false)
    @NotNull(message = "Year of manufacture is required")
    @Min(value = 1900, message = "Year of manufacture must be after 1900")
    @Max(value = 2030, message = "Year of manufacture must be before 2030")
    private Integer yearOfManufacture;

    @Column(name = "brand", length = 50, nullable = false)
    @NotBlank(message = "Brand is required")
    @Size(max = 50, message = "Brand must not exceed 50 characters")
    private String brand;

    @Column(name = "model", length = 100, nullable = false)
    @NotBlank(message = "Model is required")
    @Size(max = 100, message = "Model must not exceed 100 characters")
    private String model;

    @Column(name = "number_of_seats", nullable = false)
    @NotNull(message = "Number of seats is required")
    @Min(value = 2, message = "Number of seats must be at least 2")
    @Max(value = 16, message = "Number of seats must not exceed 16")
    private Integer numberOfSeats;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type", nullable = false)
    @NotNull(message = "Fuel type is required")
    private FuelType fuelType;

    @Enumerated(EnumType.STRING)
    @Column(name = "transmission_type", nullable = false)
    @NotNull(message = "Transmission type is required")
    private TransmissionType transmissionType;

    @Column(name = "fuel_consumption", precision = 4, scale = 2)
    @DecimalMin(value = "0.0", message = "Fuel consumption must be positive")
    @DecimalMax(value = "99.99", message = "Fuel consumption must not exceed 99.99")
    private BigDecimal fuelConsumption;

    @Column(name = "base_price_per_day", precision = 10, scale = 2, nullable = false)
    @NotNull(message = "Base price per day is required")
    @DecimalMin(value = "0.0", message = "Base price must be positive")
    private BigDecimal basePricePerDay;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", nullable = false)
    @Builder.Default
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private CarStatus status = CarStatus.ACTIVE;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Instant updatedAt;

    // == Relationships ==
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("imageOrder ASC")
    @Builder.Default
    private Set<CarImage> images = new HashSet<>();

    @OneToOne(mappedBy = "car", cascade = CascadeType.ALL)
    private CarCertificate certificate;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Trip> trips = new HashSet<>();
}
