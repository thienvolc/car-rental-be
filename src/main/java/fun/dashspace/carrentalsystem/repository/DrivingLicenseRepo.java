package fun.dashspace.carrentalsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fun.dashspace.carrentalsystem.entity.DrivingLicense;

public interface DrivingLicenseRepo extends JpaRepository<DrivingLicense, Integer> {

}