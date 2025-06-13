package fun.dashspace.carrentalsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fun.dashspace.carrentalsystem.entity.Trip;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Integer> {

    @Query("SELECT t FROM Trip t WHERE t.car.id = :carId " +
            "AND t.status = IN ('pending', 'approved', 'in_progress') " +
            "AND NOT (t.returnDate < :pickupDateTime OR t.pickupDate > :returnDateTime)")
    List<Trip> findOverlappingTrips(Integer carId, LocalDateTime pickupDateTime, LocalDateTime returnDateTime);
}