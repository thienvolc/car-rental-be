package fun.dashspace.carrentalsystem.dto.car;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class UpdateCarCertificateRequest {
    private MultipartFile registrationImage;
    private MultipartFile inspectionImage;
    private MultipartFile insuranceImage;
    private MultipartFile frontImage;
    private MultipartFile leftImage;
    private MultipartFile rightImage;
    private MultipartFile backImage;
}
