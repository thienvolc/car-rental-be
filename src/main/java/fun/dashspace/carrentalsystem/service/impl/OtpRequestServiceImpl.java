package fun.dashspace.carrentalsystem.service.impl;

import fun.dashspace.carrentalsystem.config.props.OtpProps;
import fun.dashspace.carrentalsystem.entity.OtpRequest;
import fun.dashspace.carrentalsystem.enums.OtpRequestType;
import fun.dashspace.carrentalsystem.enums.OtpStatus;
import fun.dashspace.carrentalsystem.exception.custom.validation.BadRequestException;
import fun.dashspace.carrentalsystem.exception.custom.validation.EmailNotVerifiedException;
import fun.dashspace.carrentalsystem.repository.OtpRequestRepo;
import fun.dashspace.carrentalsystem.service.NotificationService;
import fun.dashspace.carrentalsystem.service.OtpRequestService;
import fun.dashspace.carrentalsystem.util.OtpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OtpRequestServiceImpl implements OtpRequestService {
    private final OtpRequestRepo otpRequestRepo;
    private final NotificationService notificationService;
    private final OtpProps otpProps;

    private final long EXPIRATION_CLEANUP_INTERVAL = 5 * 60 * 1000; // 5 minutes

    @Override
    public void sendRegistrationOtp(String email) {
        var otpRequest = refreshPendingOtpRequest(email, OtpRequestType.REGISTRATION);
        notificationService.sendRegistrationOtp(email, otpRequest.getCode());
    }

    private OtpRequest refreshPendingOtpRequest(String email, OtpRequestType type) {
        invalidateExistingPendingOtps(email, type);
        return createOtpRequest(email, type);
    }

    public void invalidateExistingPendingOtps(String email, OtpRequestType type) {
        List<OtpRequest> pendingOtps = getExpiredPendingOtpRequests(email, type);
        pendingOtps.forEach(this::markOtpAsCancelled);
    }

    private List<OtpRequest> getExpiredPendingOtpRequests(String email, OtpRequestType type) {
        return otpRequestRepo.findAllByEmailAndRequestTypeAndStatusAndExpiredAtAfter(
                email, type, OtpStatus.PENDING, Instant.now());
    }

    private void markOtpAsCancelled(OtpRequest otpRequest) {
        updateOtpAsStatus(otpRequest, OtpStatus.CANCELLED);
    }

    private OtpRequest createOtpRequest(String email, OtpRequestType type) {
        var otpRequest = buildOtpRequest(email, type);
        otpRequestRepo.save(otpRequest);
        return otpRequest;
    }

    private OtpRequest buildOtpRequest(String email, OtpRequestType type) {
        return OtpRequest.builder()
                .email(email)
                .code(generateOtpCode())
                .requestType(type)
                .status(OtpStatus.PENDING)
                .expiredAt(Instant.now().plus(otpProps.getRequestExpiry()))
                .build();
    }

    private String generateOtpCode() {
        return OtpUtils.generateOtp(otpProps.getCodeLength());
    }

    @Override
    public void validateRegistrationEmailVerified(String email) {
        getActiveVerifiedOtpRequestsWithinType(email, OtpRequestType.REGISTRATION)
                .orElseThrow(() -> new EmailNotVerifiedException(email));
    }

    private Optional<OtpRequest> getActiveVerifiedOtpRequestsWithinType(String email, OtpRequestType type) {
        return otpRequestRepo
                .findAllByEmailAndRequestTypeAndStatus(email, type, OtpStatus.VERIFIED)
                .stream().filter(OtpRequest::isActive)
                .findFirst();
    }

    @Override
    public void verifyRegistrationOtp(String email, String otp) {
        verifyOtp(email, otp, OtpRequestType.REGISTRATION);
    }

    private void verifyOtp(String email, String code, OtpRequestType type) {
        OtpRequest otpRequest = getPendingOtpRequestOrThrow(email, code, type);
        verifyOtpExpiration(otpRequest);
        markOtpAsVerified(otpRequest);
    }

    private OtpRequest getPendingOtpRequestOrThrow(String email, String code, OtpRequestType type) {
        return otpRequestRepo
                .findByEmailAndCodeAndRequestTypeAndStatus(email, code, type, OtpStatus.PENDING)
                .orElseThrow(() -> new BadRequestException("Invalid OTP or email"));
    }

    private void verifyOtpExpiration(OtpRequest otpRequest) {
        if (otpRequest.isExpired()) {
            markOtpAsExpired(otpRequest);
            throw new BadRequestException("OTP has expired");
        }
    }

    private void markOtpAsExpired(OtpRequest otpRequest) {
        updateOtpAsStatus(otpRequest, OtpStatus.EXPIRED);
    }

    private void markOtpAsVerified(OtpRequest otpRequest) {
        updateOtpAsStatus(otpRequest, OtpStatus.VERIFIED);
    }

    private void updateOtpAsStatus(OtpRequest otpRequest, OtpStatus status) {
        otpRequest.setStatus(status);
        otpRequestRepo.save(otpRequest);
    }

    @Override
    public void sendForgotPasswordOtp(String email) {
        var otpRequest = refreshPendingOtpRequest(email, OtpRequestType.FORGOT_PASSWORD);
        notificationService.sendPasswordResetOtp(email, otpRequest.getCode());
    }

    @Override
    public void verifyForgotPasswordOtp(String email, String otp) {
        verifyOtp(email, otp, OtpRequestType.FORGOT_PASSWORD);
    }

    @Override
    public void validateForgotPasswordVerified(String email) {
        getActiveVerifiedOtpRequestsWithinType(email, OtpRequestType.FORGOT_PASSWORD)
                .orElseThrow(() -> new EmailNotVerifiedException(email));
    }

    @Override
    public void resendOtp(String email, OtpRequestType type) {
        switch (type) {
            case REGISTRATION -> sendRegistrationOtp(email);
            case FORGOT_PASSWORD -> sendForgotPasswordOtp(email);
            case HOST_REGISTRATION -> sendHostRegistrationEmailOtp(email);
            default -> throw new BadRequestException("Unsupported OTP request type: " + type);
        }
    }

    @Override
    @Scheduled(fixedRate = EXPIRATION_CLEANUP_INTERVAL)
    @Transactional
    public void cleanupExpiredOtpRequests() {
        otpRequestRepo.deleteAllByExpiredAtAfter(Instant.now());
    }

    @Override
    public void sendHostRegistrationEmailOtp(String email) {
        var otpRequest = refreshPendingOtpRequest(email, OtpRequestType.HOST_REGISTRATION);
        notificationService.sendHostRegistrationOtp(email, otpRequest.getCode());
    }

    @Override
    public void verifyHostRegistrationOtp(String email, String otpCode) {
        verifyOtp(email, otpCode, OtpRequestType.HOST_REGISTRATION);
    }

    @Override
    public void validateHostRegistrationEmailVerified(String email) {
        getActiveVerifiedOtpRequestsWithinType(email, OtpRequestType.HOST_REGISTRATION)
                .orElseThrow(() -> new EmailNotVerifiedException(email));
    }
}
