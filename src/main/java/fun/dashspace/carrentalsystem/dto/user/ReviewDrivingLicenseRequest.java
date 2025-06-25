package fun.dashspace.carrentalsystem.dto.user;

import fun.dashspace.carrentalsystem.enums.DrivingLicenseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDrivingLicenseRequest {
    private Integer drivingLicenseId;
    private DrivingLicenseStatus status;
}
