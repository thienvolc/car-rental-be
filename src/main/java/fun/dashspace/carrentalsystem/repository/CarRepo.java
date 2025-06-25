package fun.dashspace.carrentalsystem.repository;

import fun.dashspace.carrentalsystem.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CarRepo extends JpaRepository<Car, Integer> {
    Optional<Car> findById(@Param("carId") Integer carId);
}
