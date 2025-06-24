package fun.dashspace.carrentalsystem.service;

import fun.dashspace.carrentalsystem.entity.User;

import java.util.Optional;

public interface UserService {
    User createUser(String email, String password);

    User getUserByEmailOrThrow(String email);
    User getUserByIdOrThrow(Integer userId);

    boolean isEmailInUse(String email);

    void resetPassword(String email, String newPassword);

    Optional<User> getUserByEmail(String email);

    boolean isEmailValidForNewUserIdentification(String email, Integer userId);
}
