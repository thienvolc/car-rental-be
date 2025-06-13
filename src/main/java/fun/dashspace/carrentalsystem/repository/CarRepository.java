package fun.dashspace.carrentalsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fun.dashspace.carrentalsystem.entity.Car;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, Integer> {

    @Query("SELECT c FROM Car c WHERE c.location.id IN :locationIds " +
            "AND c.approvalStatus = 'approved' " +
            "AND (c.status = 'active' OR c.status = 'rented')")
    List<Car> findByLocationIds(List<Integer> locationIds);

    @Query("SELECT c FROM Car c WHERE c.location.id IN :locationIds " +
            "AND c.approvalStatus = 'approved' " +
            "AND (c.status = 'active' OR c.status = 'rented') " +
            "AND (:brand IS NULL OR c.brand = :brand) " +
            "AND (:seats IS NULL OR c.numberOfSeats = :seats) " +
            "AND (:fuelType IS NULL OR c.fuelType = :fuelType) " +
            "AND (:transmissionType IS NULL OR c.transmissionType = :transmissionType) " +
            "AND (:minPrice IS NULL OR c.basePricePerDay >= :minPrice) " +
            "AND (:maxPrice IS NULL OR c.basePricePerDay <= :maxPrice)")
    List<Car> findWithFilters(List<Integer> locationIds, String brand, Integer seats,
                              String fuelType, String transmissionType,
                              BigDecimal minPrice, BigDecimal maxPrice);

    boolean existsByLicensePlateNumber(String licensePlateNumber);
}
