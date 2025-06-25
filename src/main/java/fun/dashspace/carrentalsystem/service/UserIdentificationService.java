package fun.dashspace.carrentalsystem.service;

import fun.dashspace.carrentalsystem.dto.auth.request.HostRegistrationRequest;
import fun.dashspace.carrentalsystem.dto.host.ReviewHostIdentificationRequest;
import fun.dashspace.carrentalsystem.dto.host.GetAllHostIdentificationInfosResponse;
import fun.dashspace.carrentalsystem.dto.host.GetHostIdentificationInfoResponse;
import fun.dashspace.carrentalsystem.dto.host.HostIdentificationDto;

import java.util.Optional;

public interface UserIdentificationService {
    boolean isHostEmailUsed(String email);

    void createUserIdentification(HostRegistrationRequest req);

    Optional<GetHostIdentificationInfoResponse> getHostIdentificationInfo();

    void updateHostIdentificationStatus(ReviewHostIdentificationRequest req);

    GetAllHostIdentificationInfosResponse getAllHostIdentificationInfos();

    HostIdentificationDto getHostIdentification(Integer hostId);

    boolean isHostVerfied(Integer userId);
}
