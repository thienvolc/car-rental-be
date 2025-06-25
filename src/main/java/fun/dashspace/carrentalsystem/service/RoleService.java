package fun.dashspace.carrentalsystem.service;

import fun.dashspace.carrentalsystem.enums.RoleName;

public interface RoleService {
    void assignRoleToUser(Integer userId, RoleName roleName);
    void assignRenterRoleToUser(Integer userId);

    void assignHostRoleIfIdentificationVerified(Integer userId);
}

