package fun.dashspace.carrentalsystem.dto.host;

import fun.dashspace.carrentalsystem.enums.HostIdentificationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class HostIdentificationDto {
    private Integer userId;
    private Integer verifiedByUserId;
    private Instant verifiedAt;

    private String fullName;
    private String phoneNumber;
    private String email;

    private String nationalIdNumber;
    private String nationalIdFrontImageUrl;
    private String selfieWithNationalIdImageUrl;

    private HostIdentificationStatus status;
}
