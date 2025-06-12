package fun.dashspace.carrentalsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fun.dashspace.carrentalsystem.entity.Trip;

public interface TripRepository extends JpaRepository<Trip, Integer> {

}