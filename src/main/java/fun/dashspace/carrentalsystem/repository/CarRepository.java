package fun.dashspace.carrentalsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fun.dashspace.carrentalsystem.entity.Car;

public interface CarRepository extends JpaRepository<Car, Integer> {

}
