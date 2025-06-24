package fun.dashspace.carrentalsystem.controller;

import fun.dashspace.carrentalsystem.dto.common.response.ApiResponse;
import fun.dashspace.carrentalsystem.dto.location.DistrictListResponse;
import fun.dashspace.carrentalsystem.dto.location.ProvinceListResponse;
import fun.dashspace.carrentalsystem.dto.location.WardListResponse;
import fun.dashspace.carrentalsystem.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @GetMapping("/provinces")
    public ResponseEntity<ApiResponse<ProvinceListResponse>> getProvinces() {
        ProvinceListResponse provinces = locationService.getAllProvinces();
        return ResponseEntity.ok(ApiResponse.ok(provinces, "Provinces fetched successfully"));
    }

    @GetMapping("/districts/{provinceCode}")
    public ResponseEntity<ApiResponse<DistrictListResponse>> getDistrictsByCode(
            @PathVariable int provinceCode) {
        DistrictListResponse districts = locationService.getDistrictsByProvinceCodeAndDepth(provinceCode);
        return ResponseEntity.ok(ApiResponse.ok(districts, "Districts fetched successfully"));
    }

    @GetMapping("/wards/{districtCode}")
    public ResponseEntity<ApiResponse<WardListResponse>> getWardsByCode(
            @PathVariable int districtCode) {
        WardListResponse wards = locationService.getWardsByDistrictCodeAndDepth(districtCode);
        return ResponseEntity.ok(ApiResponse.ok(wards, "Wards fetched successfully"));
    }
}
