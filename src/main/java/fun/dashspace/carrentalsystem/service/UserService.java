package fun.dashspace.carrentalsystem.service;

import fun.dashspace.carrentalsystem.entity.User;

public interface UserService {
    User createUser(String email, String password);

    User getUserByEmailOrThrow(String email);
    User getUserByIdOrThrow(Integer userId);

    boolean existsByEmail(String email);

    void resetPassword(String email, String newPassword);
}
