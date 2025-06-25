package fun.dashspace.carrentalsystem.service;

import fun.dashspace.carrentalsystem.dto.user.*;

import java.util.Optional;

public interface DrivingLicenseService {
    void createUserDrivingLicense(CreateDrivingLicenseRequest req);

    Optional<GetDrivingLicenseResponse> getDrivingLicenseInfo();

    GetAllDrivingLicenseReviewsResponse getAllDrivingLicenseReviewInfos();

    DrivingLicenseDto getDrivingLicenseDetails(Integer drivingLicenseId);

    void updateDrivingLicenseServiceStatus(ReviewDrivingLicenseRequest req);
}
