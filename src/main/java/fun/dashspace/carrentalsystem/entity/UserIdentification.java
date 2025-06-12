package fun.dashspace.carrentalsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

import fun.dashspace.carrentalsystem.entity.base.BaseEntity;
import fun.dashspace.carrentalsystem.enums.VerificationStatus;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "user_identification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class UserIdentification extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by_user_id")
    private User verifiedByUser;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Column(name = "full_name", length = 100, nullable = false)
    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;

    @Column(name = "phone_number", length = 20, nullable = false)
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Phone number should be valid")
    private String phoneNumber;

    @Column(name = "email", length = 100, nullable = false)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Column(name = "national_id_number", length = 20, nullable = false, unique = true)
    @NotBlank(message = "National ID number is required")
    @Size(max = 20, message = "National ID number must not exceed 20 characters")
    private String nationalIdNumber;

    @Column(name = "national_id_front_image_url", length = 500, nullable = false)
    @NotBlank(message = "National ID front image is required")
    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String nationalIdFrontImageUrl;

    @Column(name = "selfie_with_national_id_image_url", length = 500, nullable = false)
    @NotBlank(message = "Selfie with national ID image is required")
    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String selfieWithNationalIdImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private VerificationStatus status = VerificationStatus.pending;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
