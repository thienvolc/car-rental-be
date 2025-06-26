package fun.dashspace.carrentalsystem.service.impl;

import fun.dashspace.carrentalsystem.dto.user.*;
import fun.dashspace.carrentalsystem.entity.User;
import fun.dashspace.carrentalsystem.exception.custom.resource.UserNotFoundException;
import fun.dashspace.carrentalsystem.exception.custom.validation.BadRequestException;
import fun.dashspace.carrentalsystem.repository.UserRepo;
import fun.dashspace.carrentalsystem.security.AuthenticateFacade;
import fun.dashspace.carrentalsystem.service.ImageUploadService;
import fun.dashspace.carrentalsystem.service.UserIdentificationService;
import fun.dashspace.carrentalsystem.service.UserService;
import fun.dashspace.carrentalsystem.util.UsernameUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserIdentificationService userIdentificationService;
    private final AuthenticateFacade authenticateFacade;
    private final ImageUploadService imageUploadService;

    @Override
    public boolean isEmailInUse(String email) {
        return userRepo.existsByEmail(email) || userIdentificationService.isHostEmailUsed(email);
    }

    @Override
    public boolean isEmailValidForNewUserIdentification(String email, Integer userId) {
        if (userIdentificationService.isHostEmailUsed(email))
            return false;
        return !isEmailUsedByOtherUser(email, userId);
    }

    private boolean isEmailUsedByOtherUser(String email, Integer userId) {
        return getUserByEmail(email)
                .map(value -> !value.getId().equals(userId))
                .orElse(false);
    }

    public User createUser(String email, String password) {
        User user = buildUser(email, passwordEncoder.encode(password));
        return userRepo.save(user);
    }

    private User buildUser(String email, String hashedPassword) {
        return User.builder()
                .email(email)
                .password(hashedPassword)
                .username(UsernameUtils.generateFromEmail(email))
                .build();
    }

    @Override
    public User getUserByEmailOrThrow(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public User getUserByIdOrThrow(Integer userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public void resetPassword(String email, String newPassword) {
        var user = getUserByEmailOrThrow(email);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public GetUserProfileResponse getCurrentUserInfo() {
        var user = authenticateFacade.getCurrentUser();
        return toUserDto(user);
    }

    private GetUserProfileResponse toUserDto(User user) {
        return GetUserProfileResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .avatarUrl(user.getAvatarUrl())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .build();
    }

    @Override
    public void updateCurrentUserInfo(UpdateUserProfileRequest req) {
        var user = authenticateFacade.getCurrentUser();
        detachUserFromDto(user, req);
        userRepo.save(user);
    }

    private void detachUserFromDto(User user, UpdateUserProfileRequest getUserProfileResponse) {
        if (getUserProfileResponse.getUsername() != null)
            user.setUsername(getUserProfileResponse.getUsername());

        if (getUserProfileResponse.getPhoneNumber() != null)
            user.setPhoneNumber(getUserProfileResponse.getPhoneNumber());

        if (getUserProfileResponse.getDateOfBirth() != null)
            user.setDateOfBirth(getUserProfileResponse.getDateOfBirth());

        if (getUserProfileResponse.getGender() != null)
            user.setGender(getUserProfileResponse.getGender());
    }

    @Override
    public void updateUserProfileAvatar(MultipartFile avatarImage) {
        var user = authenticateFacade.getCurrentUser();
        deleteOldImageIfExists(user.getAvatarUrl());
        user.setAvatarUrl(uploadImage(avatarImage));
        userRepo.save(user);
    }

    private void deleteOldImageIfExists(String avatarUrl) {
        if (avatarUrl != null)
            imageUploadService.deleteFile(avatarUrl);
    }

    private String uploadImage(MultipartFile image) {
        return imageUploadService.uploadFile(image).getSecureUrl();
    }

    @Override
    public void updateUserStatus(Integer userId, UpdateUserStatusRequest req) {
        validateNotCurrentUser(userId);
        var user = getUserByIdOrThrow(userId);
        user.setStatus(req.getStatus());
        userRepo.save(user);
    }

    private void validateNotCurrentUser(Integer userId) {
        if (authenticateFacade.getCurrentUser().getId().equals(userId))
            throw new BadRequestException("Cannot update status of the user self");
    }

    @Override
    @Transactional(readOnly = true)
    public GetAllUsersReponse getAllUsers() {
        var userList = userRepo.findAllWithDrivingLicenseAndUserIdentification();
        var userDisplayItemList = buildUserDisplayItemList(userList);
        return new GetAllUsersReponse(userDisplayItemList);
    }

    private List<UserDisplayItem> buildUserDisplayItemList(List<User> userList) {
        return userList.stream()
                .map(this::toUserDisplayItem)
                .toList();
    }

    private UserDisplayItem toUserDisplayItem(User user) {
        var userItem = UserDisplayItem.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .avatarUrl(user.getAvatarUrl())
                .status(user.getStatus())
                .build();

        if (user.getDrivingLicense() != null)
            userItem.setDrivingLicenseStatus(user.getDrivingLicense().getStatus());
        if (user.getUserIdentification() != null)
            userItem.setHostStatus(user.getUserIdentification().getStatus());

        return userItem;
    }
}