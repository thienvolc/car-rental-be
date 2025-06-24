package fun.dashspace.carrentalsystem.entity;

import fun.dashspace.carrentalsystem.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "car_locations")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = {"user", "cars"})
@ToString(callSuper = true, exclude = {"user", "cars"})
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CarLocation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;

    @Column(name = "province", length = 100, nullable = false)
    @NotBlank(message = "Province is required")
    @Size(max = 100, message = "Province must not exceed 100 characters")
    private String province;

    @Column(name = "district", length = 100, nullable = false)
    @NotBlank(message = "District is required")
    @Size(max = 100, message = "District must not exceed 100 characters")
    private String district;

    @Column(name = "ward", length = 100, nullable = false)
    @NotBlank(message = "Ward is required")
    @Size(max = 100, message = "Ward must not exceed 100 characters")
    private String ward;

    @Column(name = "address_details", columnDefinition = "TEXT", nullable = false)
    @NotBlank(message = "Address details is required")
    private String addressDetails;

    @Column(name = "latitude", precision = 10, scale = 8)
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 11, scale = 8)
    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    private BigDecimal longitude;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Instant updatedAt;

    // == Relationships ==
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Car> cars = new HashSet<>();
}
