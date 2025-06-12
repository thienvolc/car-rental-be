package fun.dashspace.carrentalsystem.repository;

import fun.dashspace.carrentalsystem.enums.OtpRequestType;
import fun.dashspace.carrentalsystem.enums.OtpStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import fun.dashspace.carrentalsystem.entity.OtpRequest;

import java.util.Optional;

public interface OtpRequestRepository extends JpaRepository<OtpRequest, Integer> {
    Optional<OtpRequest> findByEmailAndCodeAndRequestTypeAndStatus(
            String email, String code, OtpRequestType requestType, OtpStatus status
    );
    void deleteByEmailAndCode(@Email @NotBlank String email, String code);
}