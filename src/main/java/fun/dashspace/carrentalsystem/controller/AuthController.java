package fun.dashspace.carrentalsystem.controller;

import fun.dashspace.carrentalsystem.dto.auth.request.*;
import fun.dashspace.carrentalsystem.dto.auth.response.LoginResponse;
import fun.dashspace.carrentalsystem.dto.auth.response.RefreshTokenResponse;
import fun.dashspace.carrentalsystem.dto.common.response.ApiResponse;
import fun.dashspace.carrentalsystem.security.CustomUserDetails;
import fun.dashspace.carrentalsystem.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/registration/email")
    public ResponseEntity<ApiResponse<String>> sendRegistrationEmailOtp(@RequestBody RegistrationEmailRequest req) {
        authService.sendRegistrationEmailOtp(req);
        return ResponseEntity.ok(ApiResponse.ok("Email verification OTP sent successfully"));
    }

    @PostMapping("/registration/email/verify")
    public ResponseEntity<ApiResponse<String>> verifyRegistraionEmailOtp(@RequestBody VerifyOtpRequest req) {
        authService.verifyRegistraionEmailOtp(req);
        return ResponseEntity.ok(ApiResponse.ok("Email verified successfully"));
    }

    @PostMapping("/otp/resend")
    public ResponseEntity<ApiResponse<String>> resendOtp(@RequestBody ResendOtpRequest req) {
        authService.resendOtp(req);
        return ResponseEntity.ok(ApiResponse.ok("OTP resent successfully"));
    }

    @PostMapping("/renter/register")
    public ResponseEntity<ApiResponse<String>> registerRenter(@Valid @RequestBody RenterRegistrationRequest request) {
        authService.registerRenter(request);
        return ResponseEntity.ok(ApiResponse.ok("Registration initiated successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginResquest req, HttpServletRequest httpReq) {
        req.setIpAddress(httpReq.getRemoteAddr());
        req.setUserAgent(httpReq.getHeader("User-Agent"));
        req.setDeviceInfo(httpReq.getHeader("Device-Name"));
        LoginResponse res = authService.login(req);
        return ResponseEntity.ok(ApiResponse.ok(res, "Login successfully"));
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshToken(
            @RequestBody RefreshTokenRequest req, @AuthenticationPrincipal CustomUserDetails userDetails) {
        RefreshTokenResponse res = authService.refreshToken(req.getRefreshToken(), userDetails);
        return ResponseEntity.ok(ApiResponse.ok(res, "Token refreshed successfully"));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            @RequestBody LogoutRequest req, @AuthenticationPrincipal CustomUserDetails userDetails) {
        authService.logout(userDetails.getId(), req.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.ok("Logout successful"));
    }

    @PostMapping("/logout/all")
    public ResponseEntity<ApiResponse<String>> logoutAll(@AuthenticationPrincipal CustomUserDetails userDetails) {
        authService.logoutAll(userDetails.getId());
        return ResponseEntity.ok(ApiResponse.ok("Logout from all devices successful"));
    }
//
    @PostMapping("/password/forgot")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody ForgotPasswordRequest req) {
        authService.forgotPassword(req.getEmail());
        return ResponseEntity.ok(ApiResponse.ok("Password reset OTP sent successfully"));
    }

    @PostMapping("/password/verify-otp")
    public ResponseEntity<ApiResponse<String>> verifyResetOtp(@RequestBody VerifyResetOtpRequest req) {
        authService.verifyResetOtp(req);
        return ResponseEntity.ok(ApiResponse.ok("Reset OTP verified successfully"));
    }

    @PostMapping("/password/reset")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody ResetPasswordRequest req) {
        authService.resetPassword(req);
        return ResponseEntity.ok(ApiResponse.ok("Password reset completed"));
    }
}