package fun.dashspace.carrentalsystem.entity;

import fun.dashspace.carrentalsystem.entity.base.BaseEntity;
import fun.dashspace.carrentalsystem.enums.OtpRequestType;
import fun.dashspace.carrentalsystem.enums.OtpStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Entity
@Table(name = "otp_requests")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class OtpRequest extends BaseEntity {

    @Column(name = "code", length = 10, nullable = false)
    @NotBlank(message = "OTP code is required")
    @Size(min = 4, max = 10, message = "OTP code must be between 4 and 10 characters")
    private String code;

    @Column(name = "email", length = 100, nullable = false)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_type", nullable = false)
    @NotNull(message = "Request type is required")
    private OtpRequestType requestType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private OtpStatus status = OtpStatus.PENDING;

    @Column(name = "expired_at", nullable = false)
    @NotNull(message = "Expiration time is required")
    @Future(message = "Expiration time must be in the future")
    private Instant expiredAt;

    // == Helpers ==
    public boolean isActive() {
        return !isExpired();
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiredAt);
    }

    public void markAsCancelled() {
        if (OtpStatus.PENDING.equals(status))
            status = OtpStatus.CANCELLED;
    }

    public void markAsVerified() {
        if (OtpStatus.PENDING.equals(status) && !isExpired())
            status = OtpStatus.VERIFIED;
    }

    public void markAsExpired() {
        status = OtpStatus.EXPIRED;
    }
}
