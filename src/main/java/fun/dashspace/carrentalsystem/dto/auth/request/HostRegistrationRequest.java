package fun.dashspace.carrentalsystem.dto.auth.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class HostRegistrationRequest {
    private Integer userId;
    private String email;
    private String phoneNumber;
    private String fullName;
    private String nationalIdNumber;

    private MultipartFile nationalIdFrontImage;
    private MultipartFile selfieWithNationalIdImage;
}
