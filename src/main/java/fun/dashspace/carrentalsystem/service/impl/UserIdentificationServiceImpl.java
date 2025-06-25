package fun.dashspace.carrentalsystem.service.impl;

import fun.dashspace.carrentalsystem.dto.auth.request.HostRegistrationRequest;
import fun.dashspace.carrentalsystem.dto.host.*;
import fun.dashspace.carrentalsystem.entity.UserIdentification;
import fun.dashspace.carrentalsystem.exception.custom.resource.ResourceNotFoundException;
import fun.dashspace.carrentalsystem.repository.UserIdentificationRepo;
import fun.dashspace.carrentalsystem.security.AuthenticateFacade;
import fun.dashspace.carrentalsystem.service.ImageUploadService;
import fun.dashspace.carrentalsystem.service.UserIdentificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
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
    public Optional<GetHostIdentificationInfoResponse> getHostIdentificationInfo() {
        return getCurrentUserIdentification()
                .map(this::toSearchHostIdentification);
    }

    private Optional<UserIdentification> getCurrentUserIdentification() {
        return userIdentificationRepo.findByUser(authenticateFacade.getCurrentUserDetails().user());
    }

    private GetHostIdentificationInfoResponse toSearchHostIdentification(UserIdentification userIdentification) {
        return GetHostIdentificationInfoResponse.builder()
                .email(userIdentification.getEmail())
                .phoneNumber(userIdentification.getPhoneNumber())
                .status(userIdentification.getStatus())
                .build();
    }

    @Override
    public void updateHostIdentificationStatus(ReviewHostIdentificationRequest req) {
        var userIdentification = getUserIdentificationByHost(req.getHostId());
        userIdentification.setStatus(req.getStatus());
        userIdentification.setVerifiedByUser(authenticateFacade.getCurrentUserDetails().user());
        userIdentification.setVerifiedAt(Instant.now());
        userIdentificationRepo.save(userIdentification);
    }

    private UserIdentification getUserIdentificationByHost(Integer hostId) {
        return userIdentificationRepo.findByUserId(hostId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Host dentification information not found for ID: " + hostId));
    }

    @Override
    @Transactional(readOnly = true)
    public GetAllHostIdentificationInfosResponse getAllHostIdentificationInfos() {
        var userIdentificationList = userIdentificationRepo.findAll();
        var userIdentificationDtoList = toUserIdentificationReviewDtoList(userIdentificationList);
        return GetAllHostIdentificationInfosResponse.builder()
                .userIdentificationList(userIdentificationDtoList)
                .build();
    }

    private List<HostIdentificationReviewDto> toUserIdentificationReviewDtoList(
            List<UserIdentification> userIdentificationList) {
        if (userIdentificationList.isEmpty())
            return List.of();
        return userIdentificationList.stream()
                .map(this::toUserIdentificationReviewDto)
                .toList();
    }

    private HostIdentificationReviewDto toUserIdentificationReviewDto(UserIdentification userIdentification) {
        return HostIdentificationReviewDto.builder()
                .hostId(userIdentification.getUser().getId())
                .username(userIdentification.getUser().getUsername())
                .email(userIdentification.getEmail())
                .status(userIdentification.getStatus())
                .verifiedAt(userIdentification.getVerifiedAt())
                .build();
    }

    @Override
    public HostIdentificationDto getHostIdentification(Integer hostId) {
        var userIdentification = getUserIdentificationByHost(hostId);
        return toUserIdentificationDto(userIdentification);
    }

    private HostIdentificationDto toUserIdentificationDto(UserIdentification userIdentification) {
        return HostIdentificationDto.builder()
                .userId(userIdentification.getUser().getId())
                .verifiedByUserId(userIdentification.getVerifiedByUser().getId())
                .verifiedAt(userIdentification.getVerifiedAt())
                .fullName(userIdentification.getFullName())
                .phoneNumber(userIdentification.getPhoneNumber())
                .email(userIdentification.getEmail())
                .nationalIdNumber(userIdentification.getNationalIdNumber())
                .nationalIdFrontImageUrl(userIdentification.getNationalIdFrontImageUrl())
                .selfieWithNationalIdImageUrl(userIdentification.getSelfieWithNationalIdImageUrl())
                .status(userIdentification.getStatus())
                .build();
    }
}
