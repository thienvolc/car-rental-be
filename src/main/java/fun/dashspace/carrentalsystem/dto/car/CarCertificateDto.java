package fun.dashspace.carrentalsystem.dto.car;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class CarCertificateDto {
    private String registrationImageUrl;
    private String inspectionImageUrl;
    private String insuranceImageUrl;
    private String frontImageUrl;
    private String leftImageUrl;
    private String rightImageUrl;
    private String backImageUrl;
}
