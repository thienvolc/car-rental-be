package fun.dashspace.carrentalsystem.repository;

import fun.dashspace.carrentalsystem.entity.OtpRequest;
import fun.dashspace.carrentalsystem.enums.OtpRequestType;
import fun.dashspace.carrentalsystem.enums.OtpStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface OtpRequestRepo extends JpaRepository<OtpRequest, Integer> {
    Optional<OtpRequest> findByEmailAndCodeAndRequestTypeAndStatus(
            String email, String code, OtpRequestType type, OtpStatus status);

    List<OtpRequest> findAllByEmailAndRequestTypeAndStatus(String email, OtpRequestType type, OtpStatus otpStatus);

    List<OtpRequest> findAllByEmailAndRequestTypeAndStatusAndExpiredAtAfter(
            String email, OtpRequestType type, OtpStatus otpStatus, Instant now);

    void deleteAllByExpiredAtBefore(Instant now);
}