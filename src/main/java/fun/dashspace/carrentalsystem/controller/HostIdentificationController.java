package fun.dashspace.carrentalsystem.controller;

import fun.dashspace.carrentalsystem.dto.host.HostRegistrationEmailRequest;
import fun.dashspace.carrentalsystem.dto.auth.request.HostRegistrationRequest;
import fun.dashspace.carrentalsystem.dto.auth.request.VerifyOtpRequest;
import fun.dashspace.carrentalsystem.dto.common.response.ApiResponse;
import fun.dashspace.carrentalsystem.dto.host.*;
import fun.dashspace.carrentalsystem.security.CustomUserDetails;
import fun.dashspace.carrentalsystem.service.AuthService;
import fun.dashspace.carrentalsystem.service.RoleService;
import fun.dashspace.carrentalsystem.service.UserIdentificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/hosts")
@RequiredArgsConstructor
public class HostIdentificationController {
    private final UserIdentificationService userIdentificationService;
    private final RoleService roleService;
    private final AuthService authService;

    @PostMapping("/registration/email")
    public ResponseEntity<ApiResponse<String>> sendHostRegistrationEmailOtp(
            @RequestBody HostRegistrationEmailRequest req,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        req.setUserId(userDetails.getId());
        authService.sendHostRegistrationEmailOtp(req);
        return ResponseEntity.ok(ApiResponse.ok("Host email verification OTP sent successfully"));
    }

    @PostMapping("/registration/email/verify")
    public ResponseEntity<ApiResponse<String>> verifyHostRegistraionEmailOtp(@RequestBody VerifyOtpRequest req) {
        authService.verifyHostRegistraionEmailOtp(req);
        return ResponseEntity.ok(ApiResponse.ok("Host email verified successfully"));
    }

    @PostMapping(path = "/register", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
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

    @GetMapping("/registration/info")
    public ResponseEntity<ApiResponse<GetHostIdentificationInfoResponse>> getHostIdentificationInfo() {
        var hostIdentificationInfo = userIdentificationService.getHostIdentificationInfo();

        return hostIdentificationInfo
                .map(res -> ResponseEntity.ok(ApiResponse.ok(res, "Host registration info retrieved successfully")))
                .orElseGet(() -> ResponseEntity.ok(ApiResponse.notFound("Host registration info not found")));
    }

    @GetMapping("/identification/all")
    public ResponseEntity<ApiResponse<GetAllHostIdentificationInfosResponse>> getAllHostRegistrationInfos() {
        var hostIdentificationInfos = userIdentificationService.getAllHostIdentificationInfos();

        if (hostIdentificationInfos.getUserIdentificationList().isEmpty())
            return ResponseEntity.ok(ApiResponse.notFound("No host registration info found"));

        return ResponseEntity.ok(ApiResponse.ok(
                hostIdentificationInfos, "All host registration info retrieved successfully"));
    }

    @GetMapping("/identification/{hostId}")
    public ResponseEntity<ApiResponse<HostIdentificationDto>> getHostIdentification(@PathVariable Integer hostId) {
        var hostIdentification = userIdentificationService.getHostIdentification(hostId);
        return ResponseEntity.ok(ApiResponse.ok(hostIdentification, "Host verification retrieved successfully"));
    }

    @PostMapping("/identification/review")
    public ResponseEntity<ApiResponse<String>> reviewHostIdentification(@RequestBody ReviewHostIdentificationRequest req) {
        userIdentificationService.updateHostIdentificationStatus(req);
        roleService.assignHostRoleIfIdentificationVerified(req.getHostId());
        return ResponseEntity.ok(ApiResponse.ok("Updated host registration status successfully"));
    }
}
