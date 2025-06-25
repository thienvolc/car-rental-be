package fun.dashspace.carrentalsystem.dto.host;

import fun.dashspace.carrentalsystem.enums.HostIdentificationStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewHostIdentificationRequest {
    private Integer hostId;
    private HostIdentificationStatus status;
}
