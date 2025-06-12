package fun.dashspace.carrentalsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fun.dashspace.carrentalsystem.entity.CarCertificate;

public interface CarCertificateRepository extends JpaRepository<CarCertificate, Integer> {

}