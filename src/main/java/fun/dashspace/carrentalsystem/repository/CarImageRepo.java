package fun.dashspace.carrentalsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fun.dashspace.carrentalsystem.entity.CarImage;

import java.util.List;

public interface CarImageRepo extends JpaRepository<CarImage, Integer> {
    List<CarImage> findByCarIdOrderByImageOrder(Integer carId);
}