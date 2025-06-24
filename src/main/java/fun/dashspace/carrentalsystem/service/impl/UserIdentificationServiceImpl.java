package fun.dashspace.carrentalsystem.service.impl;

import fun.dashspace.carrentalsystem.dto.auth.HostRegistrationRequest;
import fun.dashspace.carrentalsystem.entity.UserIdentification;
import fun.dashspace.carrentalsystem.exception.custom.resource.ResourceNotFoundException;
import fun.dashspace.carrentalsystem.repository.UserIdentificationRepo;
import fun.dashspace.carrentalsystem.security.AuthenticateFacade;
import fun.dashspace.carrentalsystem.service.ImageUploadService;
import fun.dashspace.carrentalsystem.service.UserIdentificationService;
import fun.dashspace.carrentalsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
}
