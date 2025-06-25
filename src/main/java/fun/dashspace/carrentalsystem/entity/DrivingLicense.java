package fun.dashspace.carrentalsystem.entity;

import fun.dashspace.carrentalsystem.entity.base.BaseEntity;
import fun.dashspace.carrentalsystem.enums.HostIdentificationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Entity
@Table(name = "driving_licenses")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = {"user", "verifiedByUser"})
@ToString(callSuper = true, exclude = {"user", "verifiedByUser"})
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DrivingLicense extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by_user_id")
    private User verifiedByUser;

    @Column(name = "verified_at")
    private Instant verifiedAt;

    @Column(name = "license_number", length = 50, nullable = false, unique = true)
    @NotBlank(message = "License number is required")
    @Size(max = 50, message = "License number must not exceed 50 characters")
    private String licenseNumber;

    @Column(name = "full_name_on_license", length = 100, nullable = false)
    @NotBlank(message = "Full name on license is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullNameOnLicense;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private HostIdentificationStatus status = HostIdentificationStatus.PENDING;

    @Column(name = "license_front_image_url", length = 500, nullable = false)
    @NotBlank(message = "License front image is required")
    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String licenseFrontImageUrl;

    @Column(name = "license_back_image_url", length = 500, nullable = false)
    @NotBlank(message = "License back image is required")
    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String licenseBackImageUrl;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Instant updatedAt;
}