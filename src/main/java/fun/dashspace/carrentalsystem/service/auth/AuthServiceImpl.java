package fun.dashspace.carrentalsystem.service.auth;

import fun.dashspace.carrentalsystem.dto.request.auth.*;
import fun.dashspace.carrentalsystem.dto.response.auth.LoginResponse;
import fun.dashspace.carrentalsystem.dto.response.auth.RefreshTokenResponse;
import fun.dashspace.carrentalsystem.dto.response.auth.RegisterResponse;
import fun.dashspace.carrentalsystem.dto.response.auth.UserInfo;
import fun.dashspace.carrentalsystem.entity.OtpRequest;
import fun.dashspace.carrentalsystem.entity.Role;
import fun.dashspace.carrentalsystem.entity.User;
import fun.dashspace.carrentalsystem.entity.UserRole;
import fun.dashspace.carrentalsystem.enums.RoleType;
import fun.dashspace.carrentalsystem.enums.UserStatus;
import fun.dashspace.carrentalsystem.exception.custom.BadRequestException;
import fun.dashspace.carrentalsystem.exception.custom.InvalidOtpException;
import fun.dashspace.carrentalsystem.exception.custom.UnauthorizedException;
import fun.dashspace.carrentalsystem.exception.custom.UserNotFoundException;
import fun.dashspace.carrentalsystem.mail.EmailService;
import fun.dashspace.carrentalsystem.repository.RoleRepository;
import fun.dashspace.carrentalsystem.repository.UserRepository;
import fun.dashspace.carrentalsystem.repository.UserRoleRepository;
import fun.dashspace.carrentalsystem.security.CustomUserDetails;
import fun.dashspace.carrentalsystem.util.GenerateUsernameFromEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final OtpRequestService otpRequestService;

    private final JwtService jwtService;
    private final UserSessionService userSessionService;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;


    @Override
    public void verifyEmail(VerifyEmailRequest req) {
        checkEmailNotExists(req.getEmail());

        OtpRequest otpRequest = otpRequestService.createRegistrationOtpRequest(req.getEmail());
        emailService.sendRegistrationOtp(req.getEmail(), otpRequest.getCode());
    }

    private void checkEmailNotExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("User already exists with email: " + email);
        }
    }

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest req) {
        checkEmailNotExists(req.getEmail());
        verifyOrInvalidateOtpRequestAndThrow(req);
        User savedUser = createUser(req);
        assignDefaultRole(savedUser);

        return RegisterResponse.builder()
                .success(true)
                .message("Registration initiated. Please check your email for OTP verification.")
                .email(req.getEmail())
                .userId(savedUser.getId())
                .build();
    }

    private void verifyOrInvalidateOtpRequestAndThrow(VerifyOtpRequest req) {
        try {
            OtpRequest otpRequest = verifyRegistrationOtpOrThrow(req.getEmail(), req.getOtpCode());
            otpRequestService.markAsVerified(otpRequest.getId());
        } catch (InvalidOtpException e) {
            otpRequestService.invalidateOtpRequest(req.getEmail(), req.getOtpCode());
            throw new UnauthorizedException("Invalid or expired OTP");
        }
    }

    private OtpRequest verifyRegistrationOtpOrThrow(String email, String otpCode) {
        var otpRequest = otpRequestService.findPendingRegistrationOtpRequest(email, otpCode)
                .orElseThrow(() -> new InvalidOtpException("Invalid OTP or OTP has expired"));
        otpRequestService.checkOtpIsExpired(otpRequest);
        return otpRequest;
    }

    private User createUser(RegisterRequest req) {
        User pendingUser = User.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .username(GenerateUsernameFromEmail.generate(req.getEmail()))
                .status(UserStatus.active)
                .build();

        return userRepository.save(pendingUser);
    }

    private void assignDefaultRole(User user) {
        Role renterRole = roleRepository.findByName(RoleType.renter)
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        UserRole userRole = UserRole.builder()
                .user(user)
                .role(renterRole)
                .build();

        userRoleRepository.save(userRole);
    }

    @Override
    @Transactional
    public LoginResponse login(LoginResquest req) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            String accessToken = jwtService.generateAccessToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);
            String refreshTokenId = jwtService.extractTokenId(refreshToken);
            long expirationTime = jwtService.getExpirationTimeFromNow(refreshToken);

            var createUserSessionReq = CreateUserSessionRequest.builder()
                    .userId(userDetails.getId())
                    .refreshTokenId(refreshTokenId)
                    .ipAddress(req.getIpAddress())
                    .deviceInfo(req.getDeviceInfo())
                    .userAgent(req.getUserAgent())
                    .expirationTime(expirationTime)
                    .build();

            userSessionService.createSession(createUserSessionReq);

            return LoginResponse.builder()
                    .success(true)
                    .message("Login successful")
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .expiresIn(jwtService.getExpirationTimeFromNow(accessToken))
                    .user(UserInfo.builder()
                            .id(userDetails.getId())
                            .email(userDetails.getUsername())
                            .username(userDetails.getUsername())
                            .roles(userDetails.getAuthorities().stream()
                                    .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                                    .toList())
                            .build())
                    .build();
        } catch (BadCredentialsException e) {
            throw new BadRequestException("Invalid email or password");
        }
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest req) {
        if (!jwtService.validateToken(req.getRefreshToken()) ||
                !jwtService.isRefreshToken(req.getRefreshToken())) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        String tokenId = jwtService.extractTokenId(req.getRefreshToken());
        userSessionService.validateRefreshToken(tokenId);

        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        CustomUserDetails userDetails = new CustomUserDetails(user);
        String newAccessToken = jwtService.generateAccessToken(userDetails);
        String newRefreshToken = jwtService.generateRefreshToken(userDetails);
        String newTokenId = jwtService.extractTokenId(newRefreshToken);
        userSessionService.updateRefreshTokenSession(tokenId, newTokenId);

        return RefreshTokenResponse.builder()
                .success(true)
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .expiresIn(jwtService.getExpirationTimeFromNow(newAccessToken))
                .build();
    }

    @Override
    public void logout(String refreshToken) {
        if (jwtService.validateToken(refreshToken) && jwtService.isRefreshToken(refreshToken)) {
            String tokenId = jwtService.extractTokenId(refreshToken);
            userSessionService.deleteSession(tokenId);
        }
    }

    @Override
    public void logoutAll(Integer userId) {
        userSessionService.deleteAllSession(userId);
    }

    @Override
    public void requestPasswordReset(PasswordResetRequest req) {
        checkEmailExists(req.getEmail());
        OtpRequest otpRequest = otpRequestService.createForgotPasswordOtpRequest(req.getEmail());
        emailService.sendPasswordResetOtp(req.getEmail(), otpRequest.getCode());
    }

    @Override
    public void resetPassword(ResetPasswordRequest req) {
        checkEmailExists(req.getEmail());
        verifyOrInvalidateOtpRequestAndThrow(req);
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(user);
        logoutAll(user.getId());
    }

    private void checkEmailExists(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new UserNotFoundException("User not found with email: " + email);
        }
    }
}
