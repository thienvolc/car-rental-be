package fun.dashspace.carrentalsystem.service.impl;

import fun.dashspace.carrentalsystem.dto.auth.request.HostRegistrationRequest;
import fun.dashspace.carrentalsystem.dto.host.ReviewHostRegistraionRequest;
import fun.dashspace.carrentalsystem.dto.host.SearchHostRegistraionInfoResponse;
import fun.dashspace.carrentalsystem.entity.UserIdentification;
import fun.dashspace.carrentalsystem.exception.custom.resource.ResourceNotFoundException;
import fun.dashspace.carrentalsystem.repository.UserIdentificationRepo;
import fun.dashspace.carrentalsystem.security.AuthenticateFacade;
import fun.dashspace.carrentalsystem.service.ImageUploadService;
import fun.dashspace.carrentalsystem.service.UserIdentificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserIdentificationServiceImpl implements UserIdentificationService {
    private final UserIdentificationRepo userIdentificationRepo;
    private final AuthenticateFacade authenticateFacade;
    private final ImageUploadService imageUploadService;

    @Override
    public boolean isHostEmailUsed(String email) {
        return userIdentificationRepo.existsByEmail(email);
    }

    @Override
    public void createUserIdentification(HostRegistrationRequest req) {
        var userIdentification = toUserIdentification(req);
        userIdentification.setNationalIdFrontImageUrl(createImageUrl(req.getNationalIdFrontImage()));
        userIdentification.setSelfieWithNationalIdImageUrl(createImageUrl(req.getSelfieWithNationalIdImage()));
        userIdentificationRepo.save(userIdentification);
    }

    private String createImageUrl(MultipartFile file) {
        return imageUploadService.uploadFile(file).getSecureUrl();
    }

    private UserIdentification toUserIdentification(HostRegistrationRequest req) {
        var userDetaisl = authenticateFacade.getCurrentUserDetails();
        return UserIdentification.builder()
                .user(userDetaisl.user())
                .phoneNumber(req.getPhoneNumber())
                .fullName(req.getFullName())
                .nationalIdNumber(req.getNationalIdNumber())
                .email(req.getEmail())
                .build();
    }

    @Override
    public SearchHostRegistraionInfoResponse searchHostRegistraionInfo() {
        return getCurrentUserIdentification()
                .map(this::toSearchHostRegistraionInfo)
                .orElseThrow(() -> new ResourceNotFoundException("Host registration information not found"));
    }

    private Optional<UserIdentification> getCurrentUserIdentification() {
        return userIdentificationRepo.findByUser(authenticateFacade.getCurrentUserDetails().user());
    }

    private SearchHostRegistraionInfoResponse toSearchHostRegistraionInfo(UserIdentification userIdentification) {
        return SearchHostRegistraionInfoResponse.builder()
                .email(userIdentification.getEmail())
                .phoneNumber(userIdentification.getPhoneNumber())
                .status(userIdentification.getStatus())
                .build();
    }

    @Override
    public void updateHostRegistraionStatus(ReviewHostRegistraionRequest req) {
        var userIdentification = getUserIdentificationByHost(req.getHostId());
        userIdentification.setStatus(req.getStatus());
        userIdentification.setVerifiedByUser(authenticateFacade.getCurrentUserDetails().user());
        userIdentification.setVerifiedAt(Instant.now());
        userIdentificationRepo.save(userIdentification);
    }

    private UserIdentification getUserIdentificationByHost(Integer hostId) {
        return userIdentificationRepo.findByUserId(hostId)
                .orElseThrow(() -> new ResourceNotFoundException("Host registration information not found for ID: " + hostId));
    }
}
