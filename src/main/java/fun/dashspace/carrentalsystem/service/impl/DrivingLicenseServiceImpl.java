package fun.dashspace.carrentalsystem.service.impl;

import fun.dashspace.carrentalsystem.dto.host.HostIdentificationDto;
import fun.dashspace.carrentalsystem.dto.host.HostIdentificationReviewDto;
import fun.dashspace.carrentalsystem.dto.host.ReviewHostIdentificationRequest;
import fun.dashspace.carrentalsystem.dto.user.*;
import fun.dashspace.carrentalsystem.entity.DrivingLicense;
import fun.dashspace.carrentalsystem.entity.User;
import fun.dashspace.carrentalsystem.entity.UserIdentification;
import fun.dashspace.carrentalsystem.exception.custom.resource.ResourceNotFoundException;
import fun.dashspace.carrentalsystem.exception.custom.validation.ResourceAlreadyExistsException;
import fun.dashspace.carrentalsystem.repository.DrivingLicenseRepo;
import fun.dashspace.carrentalsystem.security.AuthenticateFacade;
import fun.dashspace.carrentalsystem.service.DrivingLicenseService;
import fun.dashspace.carrentalsystem.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DrivingLicenseServiceImpl implements DrivingLicenseService {
    private final DrivingLicenseRepo drivingLicenseRepo;
    private final ImageUploadService imageUploadService;
    private final AuthenticateFacade authenticateFacade;

    @Override
    public void createUserDrivingLicense(CreateDrivingLicenseRequest req) {
        Optional<DrivingLicense> drivingLicense = getCurrentUserDrivingLicense();
        if (drivingLicense.isPresent())
            updateUserDrivingLicense(drivingLicense.get(), req);
        else
            createDrivingLicense(req);
    }

    private Optional<DrivingLicense> getCurrentUserDrivingLicense() {
        return drivingLicenseRepo.findByUser(authenticateFacade.getCurrentUser());
    }

    private void updateUserDrivingLicense(DrivingLicense drivingLicense, CreateDrivingLicenseRequest req) {
        validateLicenseNumberNotUsedByOtherUser(req.getLicenseNumber());
        removeOldImagesIfExists(drivingLicense);
        drivingLicense.markAsPending();
        setDrivingLicenseInfo(drivingLicense, req);
        drivingLicenseRepo.save(drivingLicense);
    }

    private void validateLicenseNumberNotUsedByOtherUser(String licenseNumber) {
        var user = authenticateFacade.getCurrentUser();
        if (isLicenseNumberUsedByAnotherUser(licenseNumber, user))
            throw new ResourceAlreadyExistsException("Driving license with this number already exists.");
    }

    private boolean isLicenseNumberUsedByAnotherUser(String licenseNumber, User user) {
        var existingLicense = user.getDrivingLicense().getLicenseNumber();
        return drivingLicenseRepo.existsByLicenseNumber(licenseNumber) && !licenseNumber.equals(existingLicense);
    }

    private void removeOldImagesIfExists(DrivingLicense drivingLicense) {
        imageUploadService.deleteFile(drivingLicense.getLicenseBackImageUrl());
        imageUploadService.deleteFile(drivingLicense.getLicenseFrontImageUrl());
    }

    private void createDrivingLicense(CreateDrivingLicenseRequest req) {
        validateLicenseNumberNotExists(req.getLicenseNumber());
        var drivingLicense = buildDrivingLicense();
        setDrivingLicenseInfo(drivingLicense, req);
        drivingLicenseRepo.save(drivingLicense);
    }

    private void validateLicenseNumberNotExists(String licenseNumber) {
        if (drivingLicenseRepo.existsByLicenseNumber(licenseNumber))
            throw new ResourceAlreadyExistsException("Driving license with this number already exists.");
    }

    private DrivingLicense buildDrivingLicense() {
        return DrivingLicense.builder()
                .user(authenticateFacade.getCurrentUser())
                .build();
    }

    private void setDrivingLicenseInfo(DrivingLicense drivingLicense, CreateDrivingLicenseRequest req) {
        uploadAndSetImages(req, drivingLicense);
        drivingLicense.setLicenseNumber(req.getLicenseNumber());
        drivingLicense.setFullNameOnLicense(req.getFullNameOnLicense());
    }

    private void uploadAndSetImages(CreateDrivingLicenseRequest req, DrivingLicense drivingLicense) {
        var frontImageUrl = uploadImage(req.getLicenseFrontImage());
        var backImageUrl = uploadImage(req.getLicenseBackImage());

        drivingLicense.setLicenseFrontImageUrl(frontImageUrl);
        drivingLicense.setLicenseBackImageUrl(backImageUrl);
    }

    private String uploadImage(MultipartFile image) {
        return imageUploadService.uploadFile(image).getSecureUrl();
    }

    @Override
    public Optional<GetDrivingLicenseResponse> getDrivingLicenseInfo() {
        return getCurrentUserDrivingLicense()
                .map(this::toGetDrivingLicenseResponse);
    }

    private GetDrivingLicenseResponse toGetDrivingLicenseResponse(DrivingLicense dl) {
        return GetDrivingLicenseResponse.builder()
                .licenseNumber(dl.getLicenseNumber())
                .fullNameOnLicense(dl.getFullNameOnLicense())
                .drivingLicenseFrontImageUrl(dl.getLicenseFrontImageUrl())
                .drivingLicenseBackImageUrl(dl.getLicenseBackImageUrl())
                .status(dl.getStatus())
                .build();
    }

    @Override
    public GetAllDrivingLicenseReviewsResponse getAllDrivingLicenseReviewInfos() {
        var dlList = drivingLicenseRepo.findAll();
        var reviewList = toDrivingLicenseReviewDtoList(dlList);
        return GetAllDrivingLicenseReviewsResponse.builder()
                .drivingLicenseReviewList(reviewList)
                .build();
    }

    private List<DrivingLicenseReviewDto> toDrivingLicenseReviewDtoList(List<DrivingLicense> dls) {
        return dls.stream()
                .map(this::toDrivingLicenseReviewDto)
                .toList();
    }

    private DrivingLicenseReviewDto toDrivingLicenseReviewDto(DrivingLicense dl) {
        return DrivingLicenseReviewDto.builder()
                .id(dl.getId())
                .fullNameOnLicense(dl.getFullNameOnLicense())
                .licenseNumber(dl.getLicenseNumber())
                .verifiedAt(dl.getVerifiedAt())
                .status(dl.getStatus())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public DrivingLicenseDto getDrivingLicenseDetails(Integer drivingLicenseId) {
        var dl = getDrivingLicenseById(drivingLicenseId);
        return toDrivingLIcenseDto(dl);
    }

    private DrivingLicense getDrivingLicenseById(Integer dlId) {
        return drivingLicenseRepo.findById(dlId)
                .orElseThrow(() -> new ResourceNotFoundException("Driving license not found with id: " + dlId));
    }

    private DrivingLicenseDto toDrivingLIcenseDto(DrivingLicense dl) {
        return DrivingLicenseDto.builder()
                .id(dl.getId())
                .username(dl.getUser().getUsername())
                .verifiedByUsername(dl.getVerifiedByUser().getUsername())
                .licenseNumber(dl.getLicenseNumber())
                .fullNameOnLicense(dl.getFullNameOnLicense())
                .verifiedAt(dl.getVerifiedAt())
                .drivingLicenseBackImageUrl(dl.getLicenseBackImageUrl())
                .drivingLicenseFrontImageUrl(dl.getLicenseFrontImageUrl())
                .status(dl.getStatus())
                .build();
    }

    @Override
    public void updateDrivingLicenseServiceStatus(ReviewDrivingLicenseRequest req) {
        var dl = getDrivingLicenseById(req.getDrivingLicenseId());
        dl.setStatus(req.getStatus());
        dl.setVerifiedAt(Instant.now());
        dl.setVerifiedByUser(authenticateFacade.getCurrentUser());
        drivingLicenseRepo.save(dl);
    }
}
