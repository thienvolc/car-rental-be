package fun.dashspace.carrentalsystem.repository;

import fun.dashspace.carrentalsystem.entity.UserIdentification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserIdentificationRepo extends JpaRepository<UserIdentification, Integer> {

}