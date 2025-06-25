package fun.dashspace.carrentalsystem.service;

import fun.dashspace.carrentalsystem.dto.user.GetUserProfileResponse;
import fun.dashspace.carrentalsystem.dto.user.UpdateUserProfileRequest;
import fun.dashspace.carrentalsystem.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService {
    User createUser(String email, String password);

    User getUserByEmailOrThrow(String email);

    User getUserByIdOrThrow(Integer userId);

    boolean isEmailInUse(String email);

    void resetPassword(String email, String newPassword);

    Optional<User> getUserByEmail(String email);

    boolean isEmailValidForNewUserIdentification(String email, Integer userId);

    GetUserProfileResponse getCurrentUserInfo();

    void updateCurrentUserInfo(UpdateUserProfileRequest req);

    void updateUserProfileAvatar(MultipartFile avatarImage);
}
