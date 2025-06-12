package fun.dashspace.carrentalsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fun.dashspace.carrentalsystem.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
}