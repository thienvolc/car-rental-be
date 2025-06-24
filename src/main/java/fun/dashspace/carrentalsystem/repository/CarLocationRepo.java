package fun.dashspace.carrentalsystem.repository;

import fun.dashspace.carrentalsystem.entity.CarLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarLocationRepo extends JpaRepository<CarLocation, Integer> {

}