package fun.dashspace.carrentalsystem.repository;

import fun.dashspace.carrentalsystem.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepo extends JpaRepository<Car, Integer> {
}
