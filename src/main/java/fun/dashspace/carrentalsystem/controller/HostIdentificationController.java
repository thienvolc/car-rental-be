package fun.dashspace.carrentalsystem.controller;

import fun.dashspace.carrentalsystem.dto.common.response.ApiResponse;
import fun.dashspace.carrentalsystem.dto.host.ReviewHostRegistraionRequest;
import fun.dashspace.carrentalsystem.dto.host.SearchHostRegistraionInfoResponse;
import fun.dashspace.carrentalsystem.service.UserIdentificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hosts")
@RequiredArgsConstructor
public class HostIdentificationController {
    private final UserIdentificationService userIdentificationService;

    @GetMapping("/registration/info")
    public ResponseEntity<ApiResponse<SearchHostRegistraionInfoResponse>> searchHostRegistrationInfo() {
        var hostRegistraionInfo = userIdentificationService.searchHostRegistraionInfo();
        return ResponseEntity.ok(ApiResponse.ok(hostRegistraionInfo, "Host registration info retrieved successfully"));
    }

    @PostMapping("/registration/review")
    public ResponseEntity<ApiResponse<String>> reviewHostRegistration(@RequestBody ReviewHostRegistraionRequest req) {
        userIdentificationService.updateHostRegistraionStatus(req);
        return ResponseEntity.ok(ApiResponse.ok("Updated host registration status successfully"));
    }
}
