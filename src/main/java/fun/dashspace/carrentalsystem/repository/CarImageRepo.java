package fun.dashspace.carrentalsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fun.dashspace.carrentalsystem.entity.CarImage;

import java.util.List;
import java.util.Optional;

public interface CarImageRepo extends JpaRepository<CarImage, Integer> {
    Optional<CarImage> findByCarIdAndImageOrder(Integer carId, Integer imageOrder);

    List<CarImage> findByCarIdOrderByImageOrder(Integer carId);
}