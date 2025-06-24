package fun.dashspace.carrentalsystem.service.impl;

import fun.dashspace.carrentalsystem.entity.User;
import fun.dashspace.carrentalsystem.exception.custom.resource.UserNotFoundException;
import fun.dashspace.carrentalsystem.repository.UserRepo;
import fun.dashspace.carrentalsystem.service.UserService;
import fun.dashspace.carrentalsystem.util.UsernameUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean existsByEmail(String email) {
        return userRepo.findByEmail(email).isPresent();
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
}
