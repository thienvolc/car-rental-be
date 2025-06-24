package fun.dashspace.carrentalsystem.repository;

import fun.dashspace.carrentalsystem.entity.UserIdentification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserIdentificationRepo extends JpaRepository<UserIdentification, Integer> {

    Optional<UserIdentification> findByEmail(String email);

    boolean existsByEmail(String email);
}