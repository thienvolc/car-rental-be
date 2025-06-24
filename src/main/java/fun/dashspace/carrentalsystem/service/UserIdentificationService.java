package fun.dashspace.carrentalsystem.service;

import fun.dashspace.carrentalsystem.dto.auth.HostRegistrationRequest;

public interface UserIdentificationService {
    boolean isHostEmailUsed(String email);

    void createUserIdentification(HostRegistrationRequest req);
}
