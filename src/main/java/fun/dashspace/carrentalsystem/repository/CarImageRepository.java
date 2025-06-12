package fun.dashspace.carrentalsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fun.dashspace.carrentalsystem.entity.CarImage;

public interface CarImageRepository extends JpaRepository<CarImage, Integer> {
}