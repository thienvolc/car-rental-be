package fun.dashspace.carrentalsystem.dto.user;

import fun.dashspace.carrentalsystem.enums.DrivingLicenseStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetDrivingLicenseResponse {
    private String licenseNumber;
    private String fullNameOnLicense;

    private String drivingLicenseFrontImageUrl;
    private String drivingLicenseBackImageUrl;
    private DrivingLicenseStatus status;
}
