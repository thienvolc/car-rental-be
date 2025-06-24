package fun.dashspace.carrentalsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fun.dashspace.carrentalsystem.entity.base.BaseEntity;
import fun.dashspace.carrentalsystem.enums.Gender;
import fun.dashspace.carrentalsystem.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = {"userRoles", "ownedCars", "trips", "sessions"})
@ToString(callSuper = true, exclude = {"userRoles", "ownedCars", "trips", "sessions"})
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends BaseEntity {

    @Column(name = "username", length = 50, nullable = false, unique = true)
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @JsonIgnore
    private String password;

    @Column(name = "phone_number", length = 20)
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Phone number should be valid")
    private String phoneNumber;

    @Column(name = "date_of_birth")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "avatar_url", length = 500)
    @Size(max = 500, message = "Avatar URL must not exceed 500 characters")
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Instant updatedAt;

    // == Relationships ==
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<UserRole> userRoles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<UserSession> sessions = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private DrivingLicense drivingLicense;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserIdentification userIdentification;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Car> ownedCars = new HashSet<>();

    @OneToMany(mappedBy = "renter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Trip> trips = new HashSet<>();

    // == Helpers ==
    public boolean isActive() {
        return UserStatus.ACTIVE.equals(status);
    }

    public boolean isInactive() {
        return UserStatus.INACTIVE.equals(status);
    }

    public boolean isBanned() {
        return UserStatus.BANNED.equals(status);
    }

    public List<Role> getRoles() {
        return userRoles.stream()
                .map(UserRole::getRole)
                .toList();
    }

}