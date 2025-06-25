package fun.dashspace.carrentalsystem.dto.user;

import fun.dashspace.carrentalsystem.enums.DrivingLicenseStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class DrivingLicenseDto {
    private Integer id;

    private String username;
    private Integer verifiedByUserId;
    private Instant verifiedAt;

    private String licenseNumber;
    private String fullNameOnLicense;

    private String drivingLicenseFrontImageUrl;
    private String drivingLicenseBackImageUrl;
    private DrivingLicenseStatus status;
}
