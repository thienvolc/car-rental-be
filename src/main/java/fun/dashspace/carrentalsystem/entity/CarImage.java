package fun.dashspace.carrentalsystem.entity;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import lombok.*;

import fun.dashspace.carrentalsystem.entity.base.BaseEntity;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "car_image")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
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
    private LocalDateTime updatedAt;
}
