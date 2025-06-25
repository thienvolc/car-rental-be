package fun.dashspace.carrentalsystem.dto.user;

import fun.dashspace.carrentalsystem.enums.DrivingLicenseStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class DrivingLicenseReviewDto {
    private Integer id;
    private String licenseNumber;
    private String fullNameOnLicense;
    private DrivingLicenseStatus status;
    private Instant verifiedAt;
}
