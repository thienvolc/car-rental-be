package fun.dashspace.carrentalsystem.service.auth;

import fun.dashspace.carrentalsystem.dto.request.auth.*;
import fun.dashspace.carrentalsystem.dto.response.auth.*;
import fun.dashspace.carrentalsystem.entity.*;
import fun.dashspace.carrentalsystem.enums.*;
import fun.dashspace.carrentalsystem.exception.custom.validation.BadRequestException;
import fun.dashspace.carrentalsystem.exception.custom.auth.InvalidOtpException;
import fun.dashspace.carrentalsystem.exception.custom.auth.UnauthorizedException;
import fun.dashspace.carrentalsystem.exception.custom.resource.UserNotFoundException;
import fun.dashspace.carrentalsystem.mail.EmailService;
import fun.dashspace.carrentalsystem.repository.*;
import fun.dashspace.carrentalsystem.security.CustomUserDetails;
import fun.dashspace.carrentalsystem.util.GenerateUsernameFromEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        validateEmailNotExists(req.getEmail());
        sendVerificationOtp(req.getEmail());
    }

    private void validateEmailNotExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email already exists: " + email);
        }
    }

    private void sendVerificationOtp(String email) {
        var otpRequest = otpRequestService.createRegistrationOtp(email);
        emailService.sendRegistrationOtp(email, otpRequest.getCode());
    }

    @Override
    public RegisterResponse register(RegisterRequest req) {
        validateEmailNotExists(req.getEmail());
        validateOtp(req);
        var user = createAndSaveUser(req);
        return buildRegisterResponse(req.getEmail(), user.getId());
    }

    private void validateOtp(VerifyOtpRequest req) {
        var otpRequest = findValidOtp(req.getEmail(), req.getOtpCode());
        otpRequestService.markAsVerified(otpRequest.getId());
    }

    private OtpRequest findValidOtp(String email, String code) {
        var otpRequest = otpRequestService.findRegistrationOtp(email, code)
                .orElseThrow(() -> new InvalidOtpException("Invalid or expired OTP"));
        otpRequestService.validateOtpOrThrow(otpRequest);
        return otpRequest;
    }

    private User createAndSaveUser(RegisterRequest req) {
        var user = buildUser(req);
        var savedUser = userRepository.save(user);
        assignDefaultRole(savedUser);
        return savedUser;
    }

    private User buildUser(RegisterRequest req) {
        return User.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .username(GenerateUsernameFromEmail.generate(req.getEmail()))
                .status(UserStatus.active)
                .build();
    }

    private void assignDefaultRole(User user) {
        var role = findDefaultRole();
        var userRole = UserRole.builder().user(user).role(role).build();
        userRoleRepository.save(userRole);
    }

    private Role findDefaultRole() {
        return roleRepository.findByName(RoleType.renter)
                .orElseThrow(() -> new RuntimeException("Default role not found"));
    }

    private RegisterResponse buildRegisterResponse(String email, Integer userId) {
        return RegisterResponse.builder()
                .success(true)
                .message("Registration successful. Please verify your email.")
                .email(email)
                .userId(userId)
                .build();
    }

    @Override
    public LoginResponse login(LoginResquest req) {
        var userDetails = authenticateUser(req);
        var tokens = generateTokenPair(userDetails);
        createUserSession(req, tokens, userDetails);
        return buildLoginResponse(userDetails, tokens);
    }

    private CustomUserDetails authenticateUser(LoginResquest req) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        return (CustomUserDetails) auth.getPrincipal();
    }

    private TokenPair generateTokenPair(CustomUserDetails userDetails) {
        var accessToken = jwtService.generateAccessToken(userDetails);
        var refreshToken = jwtService.generateRefreshToken(userDetails);
        return new TokenPair(accessToken, refreshToken);
    }

    record TokenPair(String accessToken, String refreshToken) {
    }

    private void createUserSession(LoginResquest req, TokenPair tokens, CustomUserDetails userDetails) {
        var tokenId = jwtService.extractTokenId(tokens.refreshToken());
        var expirationTime = jwtService.getExpirationTimeFromNow(tokens.refreshToken());

        var sessionReq = CreateUserSessionRequest.builder()
                .userId(userDetails.getId())
                .refreshTokenId(tokenId)
                .ipAddress(req.getIpAddress())
                .deviceInfo(req.getDeviceInfo())
                .userAgent(req.getUserAgent())
                .expirationTime(expirationTime)
                .build();

        userSessionService.createSession(sessionReq);
    }

    private LoginResponse buildLoginResponse(CustomUserDetails userDetails, TokenPair tokens) {
        return LoginResponse.builder()
                .success(true)
                .message("Login successful")
                .accessToken(tokens.accessToken())
                .refreshToken(tokens.refreshToken())
                .expiresIn(jwtService.getExpirationTimeFromNow(tokens.accessToken()))
                .user(buildUserInfo(userDetails))
                .build();
    }

    private UserInfo buildUserInfo(CustomUserDetails userDetails) {
        return UserInfo.builder()
                .id(userDetails.getId())
                .email(userDetails.getUsername())
                .username(userDetails.getUsername())
                .roles(extractRoles(userDetails))
                .build();
    }

    private List<String> extractRoles(CustomUserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .toList();
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest req) {
        validateRefreshToken(req.getRefreshToken());
        var user = getUserFromToken(req.getRefreshToken());
        var tokens = generateNewTokens(user, req.getRefreshToken());
        return buildRefreshResponse(tokens);
    }

    private void validateRefreshToken(String token) {
        if (!isValidRefreshToken(token)) {
            throw new UnauthorizedException("Invalid refresh token");
        }
    }

    private boolean isValidRefreshToken(String token) {
        return jwtService.validateToken(token) && jwtService.isRefreshToken(token);
    }

    private User getUserFromToken(String refreshToken) {
        var userId = jwtService.extractUserId(refreshToken);
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private TokenPair generateNewTokens(User user, String oldRefreshToken) {
        var userDetails = new CustomUserDetails(user);
        var tokens = generateTokenPair(userDetails);
        updateRefreshTokenSession(oldRefreshToken, tokens.refreshToken());
        return tokens;
    }

    private void updateRefreshTokenSession(String oldToken, String newToken) {
        var oldTokenId = jwtService.extractTokenId(oldToken);
        var newTokenId = jwtService.extractTokenId(newToken);
        userSessionService.updateRefreshTokenId(oldTokenId, newTokenId);
    }

    private RefreshTokenResponse buildRefreshResponse(TokenPair tokens) {
        return RefreshTokenResponse.builder()
                .success(true)
                .accessToken(tokens.accessToken())
                .refreshToken(tokens.refreshToken())
                .expiresIn(jwtService.getExpirationTimeFromNow(tokens.accessToken()))
                .build();
    }

    @Override
    public void logout(String refreshToken) {
        if (isValidRefreshToken(refreshToken)) {
            deleteSession(jwtService.extractTokenId(refreshToken));
        }
    }

    private void deleteSession(String tokenId) {
        userSessionService.deleteSession(tokenId);
    }

    @Override
    public void logoutAll(Integer userId) {
        userSessionService.deleteAllSessions(userId);
    }

    @Override
    public void requestPasswordReset(PasswordResetRequest req) {
        validateEmailExists(req.getEmail());
        sendPasswordResetOtp(req.getEmail());
    }

    private void validateEmailExists(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new UserNotFoundException("Email not found: " + email);
        }
    }

    private void sendPasswordResetOtp(String email) {
        var otpRequest = otpRequestService.createForgotPasswordOtp(email);
        emailService.sendPasswordResetOtp(email, otpRequest.getCode());
    }

    @Override
    public void resetPassword(ResetPasswordRequest req) {
        validateEmailExists(req.getEmail());
        validateOtp(req);
        updateUserPassword(req);
    }

    private void updateUserPassword(ResetPasswordRequest req) {
        var user = findUserByEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(user);
        logoutAll(user.getId());
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }
}