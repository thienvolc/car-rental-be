package fun.dashspace.carrentalsystem.service.auth;

import fun.dashspace.carrentalsystem.entity.OtpRequest;
import fun.dashspace.carrentalsystem.enums.OtpRequestType;
import fun.dashspace.carrentalsystem.enums.OtpStatus;
import fun.dashspace.carrentalsystem.exception.custom.InvalidOtpException;
import fun.dashspace.carrentalsystem.exception.custom.UnauthorizedException;
import fun.dashspace.carrentalsystem.repository.OtpRequestRepository;
import fun.dashspace.carrentalsystem.util.OtpGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OtpRequestServiceImpl implements OtpRequestService {

    private final OtpRequestRepository otpRequestRepository;

    public OtpRequest createRegistrationOtpRequest(String email) {
        String otpCode = OtpGenerator.generate6DigitOtp();
        OtpRequest otpRequest = OtpRequest.builder()
                .email(email)
                .code(otpCode)
                .requestType(OtpRequestType.registration)
                .status(OtpStatus.pending)
                .expiredAt(LocalDateTime.now().plusMinutes(5))
                .build();

        return otpRequestRepository.save(otpRequest);
    }

    public void checkOtpIsExpired(OtpRequest otpRequest) throws InvalidOtpException {
        if (otpRequest.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new InvalidOtpException("OTP is expired");
        }
    }

    @Override
    public void invalidateOtpRequest(String email, String otpCode) {
        otpRequestRepository.deleteByEmailAndCode(email, otpCode);
    }

    @Transactional(readOnly = true)
    public Optional<OtpRequest> findPendingRegistrationOtpRequest(String email, String otpCode) {
        return otpRequestRepository.findByEmailAndCodeAndRequestTypeAndStatus(
                email, otpCode, OtpRequestType.registration, OtpStatus.pending);
    }

    public void markAsVerified(Integer id) {
        OtpRequest otpRequest = otpRequestRepository.findById(id)
                .orElseThrow(() -> new UnauthorizedException("Invalid OTP request ID"));
        otpRequest.setStatus(OtpStatus.verified);
        otpRequestRepository.save(otpRequest);
    }

    public OtpRequest createForgotPasswordOtpRequest(String email) {
        String otpCode = OtpGenerator.generate6DigitOtp();
        OtpRequest otpRequest = OtpRequest.builder()
                .email(email)
                .code(otpCode)
                .requestType(OtpRequestType.forgot_password)
                .status(OtpStatus.pending)
                .expiredAt(LocalDateTime.now().plusMinutes(5))
                .build();

        return otpRequestRepository.save(otpRequest);
    }

    @Transactional(readOnly = true)
    public Optional<OtpRequest> findPendingForgotPasswordOtpRequest(String email, String otpCode) {
        return otpRequestRepository
                .findByEmailAndCodeAndRequestTypeAndStatus(
                        email, otpCode, OtpRequestType.forgot_password, OtpStatus.pending);
    }
}
