package fun.dashspace.carrentalsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

import fun.dashspace.carrentalsystem.entity.base.BaseEntity;
import fun.dashspace.carrentalsystem.enums.OtpRequestType;
import fun.dashspace.carrentalsystem.enums.OtpStatus;

@Entity
@Table(name = "otp_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
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
    private OtpStatus status = OtpStatus.pending;

    @Column(name = "expired_at", nullable = false)
    @NotNull(message = "Expiration time is required")
    @Future(message = "Expiration time must be in the future")
    private LocalDateTime expiredAt;
}
