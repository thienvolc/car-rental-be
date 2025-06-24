package fun.dashspace.carrentalsystem.controller;

import fun.dashspace.carrentalsystem.dto.HostRegistrationEmailRequest;
import fun.dashspace.carrentalsystem.dto.auth.HostRegistrationRequest;
import fun.dashspace.carrentalsystem.dto.auth.request.*;
import fun.dashspace.carrentalsystem.dto.auth.response.LoginResponse;
import fun.dashspace.carrentalsystem.dto.auth.response.RefreshTokenResponse;
import fun.dashspace.carrentalsystem.dto.common.response.ApiResponse;
import fun.dashspace.carrentalsystem.security.CustomUserDetails;
import fun.dashspace.carrentalsystem.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        detachDeviceInfo(req, httpReq);
        LoginResponse res = authService.login(req);
        return ResponseEntity.ok(ApiResponse.ok(res, "Login successfully"));
    }

    private void detachDeviceInfo(@Valid LoginResquest req, HttpServletRequest httpReq) {
        var ipAddress = httpReq.getRemoteAddr();
        var userAgent = httpReq.getHeader("User-Agent");
        req.setIpAddress(ipAddress);
        req.setUserAgent(userAgent);
        req.setDeviceInfo(String.format("IP: %s | Agent: %s", ipAddress, userAgent));
    }


    @PostMapping("/token/refresh")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshToken(@RequestBody RefreshTokenRequest req) {
        RefreshTokenResponse res = authService.refreshToken(req.getRefreshToken());
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

    @PostMapping("/password/forgot/otp/verify")
    public ResponseEntity<ApiResponse<String>> verifyResetOtp(@RequestBody VerifyOtpRequest req) {
        authService.verifyResetOtp(req);
        return ResponseEntity.ok(ApiResponse.ok("Reset OTP verified successfully"));
    }

    @PostMapping("/password/reset")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody ResetPasswordRequest req) {
        authService.resetPassword(req);
        return ResponseEntity.ok(ApiResponse.ok("Password reset completed"));
    }

    @PostMapping("/host/registration/email")
    public ResponseEntity<ApiResponse<String>> sendHostRegistrationEmailOtp(
            @RequestBody HostRegistrationEmailRequest req,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        req.setUserId(userDetails.getId());
        authService.sendHostRegistrationEmailOtp(req);
        return ResponseEntity.ok(ApiResponse.ok("Host email verification OTP sent successfully"));
    }

    @PostMapping("/host/registration/email/verify")
    public ResponseEntity<ApiResponse<String>> verifyHostRegistraionEmailOtp(@RequestBody VerifyOtpRequest req) {
        authService.verifyHostRegistraionEmailOtp(req);
        return ResponseEntity.ok(ApiResponse.ok("Host email verified successfully"));
    }

    @PostMapping(path = "/host/register", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<String>> registerHost(
            @RequestPart("nationalIdFrontImage") MultipartFile nationalIdFrontImage,
            @RequestPart("selfieWithNationalIdImage") MultipartFile selfieWithNationalIdImage,
            @RequestPart("request") HostRegistrationRequest req,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        req.setNationalIdFrontImage(nationalIdFrontImage);
        req.setSelfieWithNationalIdImage(selfieWithNationalIdImage);
        req.setUserId(userDetails.getId());
        authService.registerHost(req);
        return ResponseEntity.ok(ApiResponse.ok("Host registration initiated successfully"));
    }
}