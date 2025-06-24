package fun.dashspace.carrentalsystem.entity;

import fun.dashspace.carrentalsystem.entity.base.BaseEntity;
import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Entity
@Table(name = "car_images")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = {"car"})
@ToString(callSuper = true, exclude = {"car"})
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CarImage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    @NotNull(message = "Car is required")
    private Car car;

    @Column(name = "image_order", nullable = false)
    @NotNull(message = "Image order is required")
    @Min(value = 1, message = "Image order must be at least 1")
    @Max(value = 20, message = "Image order must not exceed 20")
    private Integer imageOrder;

    @Column(name = "image_url", length = 500, nullable = false)
    @NotBlank(message = "Image URL is required")
    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String imageUrl;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Instant updatedAt;
}
