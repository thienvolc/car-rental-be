package fun.dashspace.carrentalsystem.service;

import fun.dashspace.carrentalsystem.dto.auth.request.HostRegistrationRequest;
import fun.dashspace.carrentalsystem.dto.host.ReviewHostRegistraionRequest;
import fun.dashspace.carrentalsystem.dto.host.SearchHostRegistraionInfoResponse;

public interface UserIdentificationService {
    boolean isHostEmailUsed(String email);

    void createUserIdentification(HostRegistrationRequest req);

    SearchHostRegistraionInfoResponse searchHostRegistraionInfo();

    void updateHostRegistraionStatus(ReviewHostRegistraionRequest req);
}
