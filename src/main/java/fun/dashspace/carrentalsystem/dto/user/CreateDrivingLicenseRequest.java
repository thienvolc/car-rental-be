package fun.dashspace.carrentalsystem.dto.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateDrivingLicenseRequest {
    private String licenseNumber;
    private String fullNameOnLicense;

    private MultipartFile licenseFrontImage;
    private MultipartFile licenseBackImage;
}
