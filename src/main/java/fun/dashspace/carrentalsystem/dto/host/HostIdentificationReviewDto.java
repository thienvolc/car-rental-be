package fun.dashspace.carrentalsystem.dto.host;

import fun.dashspace.carrentalsystem.enums.HostIdentificationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class HostIdentificationReviewDto {
    private Integer hostId;
    private Instant verifiedAt;
    private String username;
    private String email;
    private HostIdentificationStatus status;
}