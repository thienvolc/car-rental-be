package fun.dashspace.carrentalsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fun.dashspace.carrentalsystem.entity.CarLocation;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarLocationRepository extends JpaRepository<CarLocation, Integer> {

    @Query("SELECT cl FROM CarLocation cl WHERE " +
            "cl.province LIKE %:location% OR " +
            "cl.district LIKE %:location% OR " +
            "cl.ward LIKE %:location% OR " +
            "cl.addressDetails LIKE %:location%")
    List<CarLocation> findByLocationContaining(String location);
}