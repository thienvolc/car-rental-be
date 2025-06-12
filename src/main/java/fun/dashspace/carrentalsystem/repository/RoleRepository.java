package fun.dashspace.carrentalsystem.repository;

import fun.dashspace.carrentalsystem.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import fun.dashspace.carrentalsystem.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleType name);

    boolean existsByName(RoleType name);
}
