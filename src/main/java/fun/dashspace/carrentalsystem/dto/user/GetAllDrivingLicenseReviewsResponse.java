package fun.dashspace.carrentalsystem.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class GetAllDrivingLicenseReviewsResponse {
    List<DrivingLicenseReviewDto> drivingLicenseReviewList;
}
