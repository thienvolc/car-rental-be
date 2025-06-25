package fun.dashspace.carrentalsystem.repository;

import fun.dashspace.carrentalsystem.entity.User;
import fun.dashspace.carrentalsystem.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import fun.dashspace.carrentalsystem.entity.UserRole;

public interface UserRoleRepo extends JpaRepository<UserRole, Integer> {
    boolean existsByUserAndRoleName(User user, RoleName roleName);
}