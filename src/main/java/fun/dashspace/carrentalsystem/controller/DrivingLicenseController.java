package fun.dashspace.carrentalsystem.controller;

import fun.dashspace.carrentalsystem.dto.common.response.ApiResponse;
import fun.dashspace.carrentalsystem.dto.user.*;
import fun.dashspace.carrentalsystem.service.DrivingLicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/driving-license")
public class DrivingLicenseController {

    private final DrivingLicenseService drivingLicenseService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<String>> uploadDrivingLicense(
            @RequestPart("licenseFrontImage") MultipartFile licenseFrontImage,
            @RequestPart("licenseBackImage") MultipartFile licenseBackImage,
            @RequestPart("request") CreateDrivingLicenseRequest req) {
        req.setLicenseFrontImage(licenseFrontImage);
        req.setLicenseBackImage(licenseBackImage);
        drivingLicenseService.createUserDrivingLicense(req);
        return ResponseEntity.ok(ApiResponse.ok("Driving license initiated successfully"));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<GetDrivingLicenseResponse>> getDrivingLicenseInfo() {
        var drivingLicenseInfo = drivingLicenseService.getDrivingLicenseInfo();
        return drivingLicenseInfo
                .map(res -> ResponseEntity.ok(ApiResponse.ok(res, "Driving license info retrieved successfully")))
                .orElseGet(() -> ResponseEntity.ok(ApiResponse.notFound("Driving license info not found")));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<GetAllDrivingLicenseReviewsResponse>> getAllDrivingLicenseReviews() {
        var reviews = drivingLicenseService.getAllDrivingLicenseReviewInfos();

        if (reviews.getDrivingLicenseReviewList().isEmpty())
            return ResponseEntity.ok(ApiResponse.notFound("No driving license info found"));

        return ResponseEntity.ok(ApiResponse.ok(
                reviews, "All driving license info retrieved successfully"));
    }

    @GetMapping("/{drivingLicenseId}")
    public ResponseEntity<ApiResponse<DrivingLicenseDto>> getDrivingLicenseDetails(
            @PathVariable Integer drivingLicenseId) {
        var dl = drivingLicenseService.getDrivingLicenseDetails(drivingLicenseId);
        return ResponseEntity.ok(ApiResponse.ok(dl, "Driving license retrieved successfully"));
    }

    @PostMapping("/review")
    public ResponseEntity<ApiResponse<String>> reviewDrivingLicense(@RequestBody ReviewDrivingLicenseRequest req) {
        drivingLicenseService.updateDrivingLicenseServiceStatus(req);
        return ResponseEntity.ok(ApiResponse.ok("Updated host registration status successfully"));
    }
}
