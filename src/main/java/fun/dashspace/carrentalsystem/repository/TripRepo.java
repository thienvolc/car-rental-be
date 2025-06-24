package fun.dashspace.carrentalsystem.repository;

import fun.dashspace.carrentalsystem.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepo extends JpaRepository<Trip, Integer> {
}