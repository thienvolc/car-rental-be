package fun.dashspace.carrentalsystem.entity;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import fun.dashspace.carrentalsystem.entity.base.BaseEntity;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "trip_cancellation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class TripCancellation extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    @NotNull(message = "Trip is required")
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancelled_by_user_id", nullable = false)
    @NotNull(message = "Cancelled by user is required")
    private User cancelledByUser;

    @Column(name = "cancelled_at", nullable = false)
    @Builder.Default
    private LocalDateTime cancelledAt = LocalDateTime.now();
}