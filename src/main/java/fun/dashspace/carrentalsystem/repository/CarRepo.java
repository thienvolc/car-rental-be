package fun.dashspace.carrentalsystem.repository;

import fun.dashspace.carrentalsystem.entity.Car;
import fun.dashspace.carrentalsystem.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CarRepo extends JpaRepository<Car, Integer> {
    Optional<Car> findById(@Param("carId") Integer carId);

    boolean existsByLicensePlateNumber(String licensePlateNumber);

    @Query("SELECT c FROM Car c LEFT JOIN FETCH c.images WHERE c.id = :carId")
    Optional<Car> findByIdWithImages(@Param("carId") Integer carId);

    boolean existsByIdAndOwner(Integer carId, User currentUser);

    List<Car> findAllByOwner(User user);
}
