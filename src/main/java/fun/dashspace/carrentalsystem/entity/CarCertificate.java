package fun.dashspace.carrentalsystem.entity;

import fun.dashspace.carrentalsystem.entity.base.BaseEntity;
import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Entity
@Table(name = "car_certificates")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = {"car"})
@ToString(callSuper = true, exclude = {"car"})
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CarCertificate extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    @NotNull(message = "Car is required")
    private Car car;

    @Column(name = "registration_url", length = 500)
    @Size(max = 500, message = "Registration URL must not exceed 500 characters")
    private String registrationUrl;

    @Column(name = "inspection_url", length = 500)
    @Size(max = 500, message = "Inspection URL must not exceed 500 characters")
    private String inspectionUrl;

    @Column(name = "insurance_url", length = 500)
    @Size(max = 500, message = "Insurance URL must not exceed 500 characters")
    private String insuranceUrl;

    @Column(name = "front_image_url", length = 500)
    @Size(max = 500, message = "Front image URL must not exceed 500 characters")
    private String frontImageUrl;

    @Column(name = "left_image_url", length = 500)
    @Size(max = 500, message = "Left image URL must not exceed 500 characters")
    private String leftImageUrl;

    @Column(name = "right_image_url", length = 500)
    @Size(max = 500, message = "Right image URL must not exceed 500 characters")
    private String rightImageUrl;

    @Column(name = "back_image_url", length = 500)
    @Size(max = 500, message = "Back image URL must not exceed 500 characters")
    private String backImageUrl;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Instant updatedAt;
}