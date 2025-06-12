package fun.dashspace.carrentalsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fun.dashspace.carrentalsystem.entity.CarLocation;

public interface CarLocationRepository extends JpaRepository<CarLocation, Integer> {

}