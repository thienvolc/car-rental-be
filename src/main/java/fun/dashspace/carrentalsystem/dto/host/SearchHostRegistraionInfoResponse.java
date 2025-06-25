package fun.dashspace.carrentalsystem.dto.host;

import fun.dashspace.carrentalsystem.enums.HostIdentificationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SearchHostRegistraionInfoResponse {
    private String email;
    private String phoneNumber;
    private HostIdentificationStatus status;
}
