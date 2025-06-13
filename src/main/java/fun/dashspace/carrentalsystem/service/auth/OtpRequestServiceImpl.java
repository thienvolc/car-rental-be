package fun.dashspace.carrentalsystem.service.auth;

import fun.dashspace.carrentalsystem.config.properties.OtpProperties;
import fun.dashspace.carrentalsystem.entity.OtpRequest;
import fun.dashspace.carrentalsystem.enums.OtpRequestType;
import fun.dashspace.carrentalsystem.enums.OtpStatus;
import fun.dashspace.carrentalsystem.exception.custom.auth.InvalidOtpException;
import fun.dashspace.carrentalsystem.repository.OtpRequestRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpRequestServiceImpl implements OtpRequestService {

    private final OtpRequestRepository otpRequestRepository;
    private final int otpExpirationMinutes;
    private final int optLength;

    OtpRequestServiceImpl(
            OtpProperties props, OtpRequestRepository repository) {
        this.otpRequestRepository = repository;
        this.otpExpirationMinutes = props.getOtpExpirationMinutes();
        this.optLength = props.getOptLength();
    }

    @Override
    public OtpRequest createOtpRequest(String email, OtpRequestType type) {
        invalidateExistingOtps(email, type);
        var otpRequest = buildOtpRequest(email, type);
        return otpRequestRepository.save(otpRequest);

    }

    private void invalidateExistingOtps(String email, OtpRequestType type) {
        otpRequestRepository.findByEmailAndRequestTypeAndStatus(email, type, OtpStatus.pending)
                .forEach(otp -> updateOtpStatus(otp.getId(), OtpStatus.failed));
    }

    private void updateOtpStatus(Integer otpId, OtpStatus status) {
        otpRequestRepository.findById(otpId)
                .ifPresent(otp -> {
                    otp.setStatus(status);
                    otpRequestRepository.save(otp);
                });
    }

    private OtpRequest buildOtpRequest(String email, OtpRequestType type) {
        return OtpRequest.builder()
                .email(email)
                .code(generateOtpCode())
                .requestType(type)
                .status(OtpStatus.pending)
                .expiredAt(calculateExpirationTime())
                .build();
    }

    private String generateOtpCode() {
        var random = new Random();
        var code = new StringBuilder();
        for (int i = 0; i < optLength; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    private LocalDateTime calculateExpirationTime() {
        return LocalDateTime.now().plusMinutes(otpExpirationMinutes);
    }

    @Override
    public Optional<OtpRequest> findPendingOtpRequest(String email, String code, OtpRequestType type) {
        return otpRequestRepository.findByEmailAndCodeAndRequestTypeAndStatus(
                email, code, type, OtpStatus.pending);
    }

    @Override
    public void markAsVerified(Integer otpId) {
        updateOtpStatus(otpId, OtpStatus.verified);
    }

    @Override
    public void markAsExpired(Integer otpId) {
        updateOtpStatus(otpId, OtpStatus.expired);
    }

    @Override
    public void invalidateOtpRequest(String email, String code) {
        otpRequestRepository.findByEmailAndCode(email, code)
                .ifPresent(otp -> updateOtpStatus(otp.getId(), OtpStatus.failed));
    }

    @Override
    public boolean isOtpExpired(OtpRequest otpRequest) {
        return otpRequest.getExpiredAt().isBefore(LocalDateTime.now());
    }

    @Override
    public void validateOtpOrThrow(OtpRequest otpRequest) {
        if (isOtpExpired(otpRequest)) {
            markAsExpired(otpRequest.getId());
            throw new InvalidOtpException("OTP has expired");
        }
    }

    private final int ONE_HOUR_IN_MS = 3600000;

    @Scheduled(fixedRate = ONE_HOUR_IN_MS)
    @Transactional
    public void cleanUpExpiredOtps() {
        otpRequestRepository.cleanupOldOtps(LocalDateTime.now());
    }
}
