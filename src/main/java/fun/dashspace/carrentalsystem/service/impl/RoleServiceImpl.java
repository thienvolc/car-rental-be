package fun.dashspace.carrentalsystem.service.impl;

import fun.dashspace.carrentalsystem.entity.Role;
import fun.dashspace.carrentalsystem.entity.User;
import fun.dashspace.carrentalsystem.entity.UserRole;
import fun.dashspace.carrentalsystem.enums.RoleName;
import fun.dashspace.carrentalsystem.exception.custom.resource.ResourceNotFoundException;
import fun.dashspace.carrentalsystem.repository.RoleRepo;
import fun.dashspace.carrentalsystem.repository.UserRoleRepo;
import fun.dashspace.carrentalsystem.service.RoleService;
import fun.dashspace.carrentalsystem.service.UserIdentificationService;
import fun.dashspace.carrentalsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepo roleRepository;
    private final UserRoleRepo userRoleRepository;
    private final UserService userService;
    private final UserIdentificationService userIdentificationService;

    @Override
    public void assignRoleToUser(Integer userId, RoleName roleName) {
        User user = userService.getUserByIdOrThrow(userId);
        createUserRoleIfNotExists(user, roleName);
    }

    private void createUserRoleIfNotExists(User user, RoleName roleName) {
        if (isRoleNotExistsByUser(user, roleName))
            createUserRole(user, roleName);
    }

    private boolean isRoleNotExistsByUser(User user, RoleName roleName) {
        return !userRoleRepository.existsByUserAndRoleName(user, roleName);
    }

    private void createUserRole(User user, RoleName roleName) {
        var role = getRoleByNameOrThrow(roleName);
        var userRole = buildUserRole(user, role);
        userRoleRepository.save(userRole);
    }

    private Role getRoleByNameOrThrow(RoleName roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role " + roleName + " not found"));
    }

    private UserRole buildUserRole(User user, Role role) {
        return UserRole.builder()
                .user(user)
                .role(role)
                .build();
    }

    @Override
    public void assignRenterRoleToUser(Integer userId) {
        assignRoleToUser(userId, RoleName.RENTER);
    }

    @Override
    public void assignHostRoleIfIdentificationVerified(Integer userId) {
        if (userIdentificationService.isHostVerfied(userId))
            assignRoleToUser(userId, RoleName.HOST);
    }
}
