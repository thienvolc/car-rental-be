package fun.dashspace.carrentalsystem.dto.user;

import fun.dashspace.carrentalsystem.enums.DrivingLicenseStatus;
import fun.dashspace.carrentalsystem.enums.HostIdentificationStatus;
import fun.dashspace.carrentalsystem.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDisplayItem {
    private Integer id;
    private String username;
    private String email;
    private String avatarUrl;
    private String phoneNumber;
    private UserStatus status;
    private HostIdentificationStatus hostStatus;
    private DrivingLicenseStatus drivingLicenseStatus;
}
