package fun.dashspace.carrentalsystem.service.impl;

import fun.dashspace.carrentalsystem.dto.HostRegistrationEmailRequest;
import fun.dashspace.carrentalsystem.dto.auth.*;
import fun.dashspace.carrentalsystem.dto.auth.request.*;
import fun.dashspace.carrentalsystem.dto.auth.response.LoginResponse;
import fun.dashspace.carrentalsystem.dto.auth.response.RefreshTokenResponse;
import fun.dashspace.carrentalsystem.exception.custom.validation.BadRequestException;
import fun.dashspace.carrentalsystem.repository.*;
import fun.dashspace.carrentalsystem.security.CustomUserDetails;
import fun.dashspace.carrentalsystem.service.*;
import fun.dashspace.carrentalsystem.service.factory.UserFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final OtpRequestService otpRequestService;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final UserSessionService userSessionService;
    private final AuthenticationManager authenticationManager;
    private final UserFactory userFactory;
    private final UserService userService;
    private final UserIdentificationService userIdentificationService;

    @Override
    public void sendRegistrationEmailOtp(RegistrationEmailRequest req) {
        validateEmailNotExists(req.getEmail());
        otpRequestService.sendRegistrationOtp(req.getEmail());
    }

    private void validateEmailNotExists(String email) {
        if (userService.isEmailInUse(email))
            throw new BadRequestException("Email already exists: " + email);
    }

    @Override
    public void verifyRegistraionEmailOtp(VerifyOtpRequest req) {
        otpRequestService.verifyRegistrationOtp(req.getEmail(), req.getOtpCode());
    }

    @Override
    public void registerRenter(RenterRegistrationRequest req) {
        validateEmailNotExists(req.getEmail());
        otpRequestService.validateRegistrationEmailVerified(req.getEmail());
        userFactory.createRenter(req.getEmail(), req.getPassword());
    }

    @Override
    public LoginResponse login(LoginResquest req) {
        var userDetails = authenticateUser(req.getEmail(), req.getPassword());
        var tokens = tokenService.generateTokenPair(userDetails);
        createUserSession(userDetails.getId(), req, tokens.refreshToken());
        return buildLoginResponse(userDetails, tokens);
    }

    private CustomUserDetails authenticateUser(String email, String password) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        return (CustomUserDetails) auth.getPrincipal();
    }

    private void createUserSession(Integer userId, LoginResquest req, String refreshToken) {
        var sessionReq = toSessionRequest(userId, req, refreshToken);
        userSessionService.createSession(sessionReq);
    }

    private CreateUserSessionRequest toSessionRequest(Integer userId, LoginResquest req, String refreshToken) {
        return CreateUserSessionRequest.builder()
                .userId(userId)
                .refreshTokenId(jwtService.extractTokenId(refreshToken))
                .ipAddress(req.getIpAddress())
                .deviceInfo(req.getDeviceInfo())
                .userAgent(req.getUserAgent())
                .expiryTime(jwtService.getExpiryTime(refreshToken))
                .build();
    }

    private LoginResponse buildLoginResponse(CustomUserDetails userDetails, TokenPair tokens) {
        return LoginResponse.builder()
                .accessToken(tokens.accessToken())
                .refreshToken(tokens.refreshToken())
                .expiryTime(jwtService.getExpiryTime(tokens.accessToken()))
                .userId(userDetails.getId())
                .email(userDetails.getUsername())
                .username(userDetails.getDisplayName())
                .roles(userDetails.getRoleNames())
                .build();
    }

    @Override
    public RefreshTokenResponse refreshToken(String refreshToken) {
        var user = userService.getUserByIdOrThrow(jwtService.extractUserId(refreshToken));
        var userDetails = new CustomUserDetails(user);
        try {
            TokenPair tokens = tokenService.generateTokenPair(userDetails);
            var newRefreshTokenId = jwtService.extractTokenId(tokens.refreshToken());
            var oldRefreshTokenId = jwtService.extractTokenId(refreshToken);
            userSessionService.refreshUserSession(userDetails.getId(), oldRefreshTokenId, newRefreshTokenId);
            return buildRefreshTokenResponse(userDetails, tokens);
        } catch (Exception e) {
            throw new BadRequestException("Refreshing token error: " + e.getMessage());
        }
    }


    private RefreshTokenResponse buildRefreshTokenResponse(CustomUserDetails userDetails, TokenPair tokens) {
        return RefreshTokenResponse.builder()
                .accessToken(tokens.accessToken())
                .refreshToken(tokens.refreshToken())
                .userId(userDetails.getId())
                .userName(userDetails.getDisplayName())
                .email(userDetails.getUsername())
                .expiryTime(jwtService.getExpiryTime(tokens.accessToken()))
                .roles(userDetails.getRoleNames())
                .build();
    }

    @Override
    public void logout(Integer userId, String refreshToken) {
        userSessionService.invalidateSession(userId, jwtService.extractTokenId(refreshToken));
    }

    @Override
    public void logoutAll(Integer userId) {
        userSessionService.invalidateAllSessions(userId);
    }

    @Override
    public void resendOtp(ResendOtpRequest req) {
        otpRequestService.resendOtp(req.getEmail(), req.getType());
    }

    @Override
    public void forgotPassword(String email) {
        validateEmailExists(email);
        otpRequestService.sendForgotPasswordOtp(email);
    }

    private void validateEmailExists(String email) {
        if (!userRepo.existsByEmail(email)) {
            throw new BadRequestException("Email does not exist: " + email);
        }
    }

    @Override
    public void verifyResetOtp(VerifyOtpRequest req) {
        otpRequestService.verifyForgotPasswordOtp(req.getEmail(), req.getOtpCode());
    }

    @Override
    public void resetPassword(ResetPasswordRequest req) {
        validateEmailExists(req.getEmail());
        otpRequestService.validateForgotPasswordVerified(req.getEmail());
        userService.resetPassword(req.getEmail(), req.getNewPassword());
    }

    @Override
    public void sendHostRegistrationEmailOtp(HostRegistrationEmailRequest req) {
        validateEmailForHostRegistration(req.getEmail(), req.getUserId());
        otpRequestService.sendHostRegistrationEmailOtp(req.getEmail());
    }

    private void validateEmailForHostRegistration(String email, Integer userId) {
        if (!userService.isEmailValidForNewUserIdentification(email, userId))
            throw new BadRequestException("Email is already in use or invalid for host registration.");
    }

    @Override
    public void verifyHostRegistraionEmailOtp(VerifyOtpRequest req) {
        otpRequestService.verifyHostRegistrationOtp(req.getEmail(), req.getOtpCode());
    }

    @Override
    public void registerHost(HostRegistrationRequest req) {
        validateEmailForHostRegistration(req.getEmail(), req.getUserId());
        otpRequestService.validateHostRegistrationEmailVerified(req.getEmail());
        userIdentificationService.createUserIdentification(req);
    }
}