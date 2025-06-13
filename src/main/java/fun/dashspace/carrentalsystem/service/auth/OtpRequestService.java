package fun.dashspace.carrentalsystem.service.auth;

import fun.dashspace.carrentalsystem.entity.OtpRequest;

import java.util.Optional;

import fun.dashspace.carrentalsystem.enums.OtpRequestType;

public interface OtpRequestService {

    OtpRequest createOtpRequest(String email, OtpRequestType type);
    Optional<OtpRequest> findPendingOtpRequest(String email, String code, OtpRequestType type);

    void markAsVerified(Integer otpId);
    void markAsExpired(Integer otpId);
    void invalidateOtpRequest(String email, String code);

    boolean isOtpExpired(OtpRequest otpRequest);
    void validateOtpOrThrow(OtpRequest otpRequest);

    default OtpRequest createRegistrationOtp(String email) {
        return createOtpRequest(email, OtpRequestType.registration);
    }

    default OtpRequest createForgotPasswordOtp(String email) {
        return createOtpRequest(email, OtpRequestType.forgot_password);
    }

    default Optional<OtpRequest> findRegistrationOtp(String email, String code) {
        return findPendingOtpRequest(email, code, OtpRequestType.registration);
    }

    default Optional<OtpRequest> findForgotPasswordOtp(String email, String code) {
        return findPendingOtpRequest(email, code, OtpRequestType.forgot_password);
    }
}
