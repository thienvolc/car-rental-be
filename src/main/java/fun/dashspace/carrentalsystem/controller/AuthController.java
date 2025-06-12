package fun.dashspace.carrentalsystem.controller;

import fun.dashspace.carrentalsystem.dto.request.auth.*;
import fun.dashspace.carrentalsystem.dto.response.auth.LoginResponse;
import fun.dashspace.carrentalsystem.dto.response.auth.RefreshTokenResponse;
import fun.dashspace.carrentalsystem.dto.response.auth.RegisterResponse;
import fun.dashspace.carrentalsystem.dto.response.common.ApiResponse;
import fun.dashspace.carrentalsystem.service.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<RegisterResponse>builder()
                        .success(true)
                        .message("Registration initiated successfully")
                        .data(response)
                        .build());
    }

    @PostMapping("/verify-registration")
    public ResponseEntity<ApiResponse<String>> verifyRegistration(@Valid @RequestBody VerifyEmailRequest request) {
        authService.verifyEmail(request);

        return ResponseEntity.ok(ApiResponse.<String>builder()
                .success(true)
                .message("Registration verified successfully")
                .data("Account activated")
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginResquest request,
            HttpServletRequest httpRequest) {

        // Add request info for session tracking
        request.setIpAddress(getClientIpAddress(httpRequest));
        request.setUserAgent(httpRequest.getHeader("User-Agent"));
        if (request.getDeviceInfo() == null) {
            request.setDeviceInfo(extractDeviceInfo(httpRequest));
        }

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(ApiResponse.<LoginResponse>builder()
                .success(true)
                .message("Login successful")
                .data(response)
                .build());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        RefreshTokenResponse response = authService.refreshToken(request);

        return ResponseEntity.ok(ApiResponse.<RefreshTokenResponse>builder()
                .success(true)
                .message("Token refreshed successfully")
                .data(response)
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestBody RefreshTokenRequest request) {
        authService.logout(request.getRefreshToken());

        return ResponseEntity.ok(ApiResponse.<String>builder()
                .success(true)
                .message("Logout successful")
                .data("Session terminated")
                .build());
    }

    @PostMapping("/logout-all")
    public ResponseEntity<ApiResponse<String>> logoutAll(@RequestBody RefreshTokenRequest request) {
        authService.logoutAll(request.getUserId());

        return ResponseEntity.ok(ApiResponse.<String>builder()
                .success(true)
                .message("Logged out from all devices")
                .data("All sessions terminated")
                .build());
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@Valid @RequestBody PasswordResetRequest request) {
        authService.requestPasswordReset(request);

        return ResponseEntity.ok(ApiResponse.<String>builder()
                .success(true)
                .message("Password reset OTP sent to your email")
                .data("Check your email")
                .build());
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);

        return ResponseEntity.ok(ApiResponse.<String>builder()
                .success(true)
                .message("Password reset successful")
                .data("Password updated")
                .build());
    }

    // Helper methods
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }

    private String extractDeviceInfo(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return userAgent != null ? userAgent.substring(0, Math.min(userAgent.length(), 255)) : "Unknown";
    }
}