package fun.dashspace.carrentalsystem.service.auth;

import fun.dashspace.carrentalsystem.entity.OtpRequest;

import java.util.Optional;

public interface OtpRequestService {
    OtpRequest createRegistrationOtpRequest(String email);

    void checkOtpIsExpired(OtpRequest otpRequest);

    void invalidateOtpRequest(String email, String otpCode);

    Optional<OtpRequest> findPendingRegistrationOtpRequest(String email, String otpCode);

    void markAsVerified(Integer id);

    OtpRequest createForgotPasswordOtpRequest(String email);

    Optional<OtpRequest> findPendingForgotPasswordOtpRequest(String email, String otpCode);
}
