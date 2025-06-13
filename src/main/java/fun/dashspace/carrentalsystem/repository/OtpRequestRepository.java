package fun.dashspace.carrentalsystem.repository;

import fun.dashspace.carrentalsystem.enums.OtpRequestType;
import fun.dashspace.carrentalsystem.enums.OtpStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import fun.dashspace.carrentalsystem.entity.OtpRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OtpRequestRepository extends JpaRepository<OtpRequest, Integer> {
    Optional<OtpRequest> findByEmailAndCodeAndRequestTypeAndStatus(
            String email, String code, OtpRequestType type, OtpStatus status);

    Optional<OtpRequest> findByEmailAndCode(String email, String code);

    List<OtpRequest> findByEmailAndRequestTypeAndStatus(String email, OtpRequestType type, OtpStatus status);

    @Modifying
    @Query("UPDATE OtpRequest o SET o.status = 'expired' WHERE o.expiredAt < :now AND o.status = 'pending'")
    void expireOldOtps(@Param("now") LocalDateTime now);

    @Modifying
    @Query("DELETE FROM OtpRequest o WHERE o.createdAt < :cutoff")
    void cleanupOldOtps(@Param("cutoff") LocalDateTime cutoff);
}