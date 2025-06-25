package fun.dashspace.carrentalsystem.repository;

import fun.dashspace.carrentalsystem.entity.User;
import fun.dashspace.carrentalsystem.entity.DrivingLicense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DrivingLicenseRepo extends JpaRepository<DrivingLicense, Integer> {
    Optional<DrivingLicense> findByUser(User user);

    boolean existsByLicenseNumber(String licenseNumber);

    boolean existsByUser(User user);
}