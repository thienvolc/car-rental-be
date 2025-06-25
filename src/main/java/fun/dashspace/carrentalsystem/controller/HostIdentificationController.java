package fun.dashspace.carrentalsystem.controller;

import fun.dashspace.carrentalsystem.dto.common.response.ApiResponse;
import fun.dashspace.carrentalsystem.dto.host.*;
import fun.dashspace.carrentalsystem.service.RoleService;
import fun.dashspace.carrentalsystem.service.UserIdentificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hosts")
@RequiredArgsConstructor
public class HostIdentificationController {
    private final UserIdentificationService userIdentificationService;
    private final RoleService roleService;

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
