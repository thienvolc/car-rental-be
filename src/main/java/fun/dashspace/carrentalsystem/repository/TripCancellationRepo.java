package fun.dashspace.carrentalsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fun.dashspace.carrentalsystem.entity.TripCancellation;

public interface TripCancellationRepo extends JpaRepository<TripCancellation, Integer> {
}