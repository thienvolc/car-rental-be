package fun.dashspace.carrentalsystem.repository;

import fun.dashspace.carrentalsystem.entity.Role;
import fun.dashspace.carrentalsystem.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleName name);
}
