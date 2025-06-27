package fun.dashspace.carrentalsystem.repository;

import fun.dashspace.carrentalsystem.entity.Car;
import fun.dashspace.carrentalsystem.entity.Trip;
import fun.dashspace.carrentalsystem.enums.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public interface TripRepo extends JpaRepository<Trip, Integer> {

    boolean existsByCarAndStatusInAndPickupDateLessThanEqualAndReturnDateGreaterThanEqual(
            Car car, List<TripStatus> notAvaible, Instant endTime, Instant startTime);
}