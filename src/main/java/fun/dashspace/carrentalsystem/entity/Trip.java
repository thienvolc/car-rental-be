package fun.dashspace.carrentalsystem.entity;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import fun.dashspace.carrentalsystem.entity.base.BaseEntity;
import fun.dashspace.carrentalsystem.enums.TripStatus;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "trip")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"cancellation"})
@ToString(exclude = {"cancellation"})
public class Trip extends BaseEntity {

    @Column(name = "trip_code", length = 20, nullable = false, unique = true)
    @NotBlank(message = "Trip code is required")
    @Size(max = 20, message = "Trip code must not exceed 20 characters")
    private String tripCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    @NotNull(message = "Car is required")
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renter_id", nullable = false)
    @NotNull(message = "Renter is required")
    private User renter;

    @Column(name = "pickup_date", nullable = false)
    @NotNull(message = "Pickup date is required")
    @Future(message = "Pickup date must be in the future")
    private LocalDateTime pickupDate;

    @Column(name = "return_date", nullable = false)
    @NotNull(message = "Return date is required")
    private LocalDateTime returnDate;

    @Column(name = "total_amount", precision = 12, scale = 2, nullable = false)
    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", message = "Total amount must be positive")
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private TripStatus status = TripStatus.pending;

    @Column(name = "approval_time")
    private LocalDateTime approvalTime;

    @OneToOne(mappedBy = "trip", cascade = CascadeType.ALL)
    private TripCancellation cancellation;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    private void validateDates() {
        if (pickupDate != null && returnDate != null && !returnDate.isAfter(pickupDate)) {
            throw new IllegalArgumentException("Return date must be after pickup date");
        }
    }
}