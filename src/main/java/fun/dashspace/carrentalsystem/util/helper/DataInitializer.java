package fun.dashspace.carrentalsystem.util.helper;

import fun.dashspace.carrentalsystem.entity.Role;
import fun.dashspace.carrentalsystem.entity.User;
import fun.dashspace.carrentalsystem.entity.UserRole;
import fun.dashspace.carrentalsystem.enums.RoleType;
import fun.dashspace.carrentalsystem.enums.UserStatus;
import fun.dashspace.carrentalsystem.repository.RoleRepository;
import fun.dashspace.carrentalsystem.repository.UserRepository;
import fun.dashspace.carrentalsystem.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        // Create roles first
        createRoleIfNotExists(RoleType.admin);
        createRoleIfNotExists(RoleType.renter);
        createRoleIfNotExists(RoleType.host);

        // Create users
        User admin = createUser("admin", "admin@dashspace.fun", "Admin@123456");
        assignRole(admin, RoleType.admin);

        User renter = createUser("renter", "renter@dashspace.fun", "Renter@123456");
        assignRole(renter, RoleType.renter);

        log.info("Admin credentials: username: admin, email: admin@dashspace.fun, password: Admin@123456");
        log.info("Renter credentials: username: renter, email: renter@dashspace.fun, password: Renter@123456");
    }

    private void createRoleIfNotExists(RoleType roleType) {
        if (!roleRepository.existsByName(roleType)) {
            Role role = new Role();
            role.setName(roleType);
            role.setDescription(roleType.name());
            role.setCreatedAt(LocalDateTime.now());
            roleRepository.save(role);
        }
    }

    private User createUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setDateOfBirth(LocalDate.of(1990, 1, 1));
        user.setStatus(UserStatus.active);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    private void assignRole(User user, RoleType roleType) {
        Role role = roleRepository.findByName(roleType)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleType));

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        userRole.setCreatedAt(LocalDateTime.now());
        userRoleRepository.save(userRole);
    }
}