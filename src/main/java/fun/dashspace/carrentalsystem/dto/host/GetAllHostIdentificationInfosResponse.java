package fun.dashspace.carrentalsystem.dto.host;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class GetAllHostIdentificationInfosResponse {
    List<HostIdentificationReviewDto> userIdentificationList;
}
