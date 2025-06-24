package fun.dashspace.carrentalsystem.entity;

import fun.dashspace.carrentalsystem.entity.base.BaseEntity;
import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Entity
@Table(name = "trip_cancellations")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = {"trip", "cancelledByUser"})
@ToString(callSuper = true, exclude = {"trip", "cancelledByUser"})
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
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
    private Instant cancelledAt = Instant.now();
}