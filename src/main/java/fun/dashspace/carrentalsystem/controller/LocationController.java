package fun.dashspace.carrentalsystem.controller;

import fun.dashspace.carrentalsystem.dto.location.District;
import fun.dashspace.carrentalsystem.dto.location.Province;
import fun.dashspace.carrentalsystem.dto.location.Ward;
import fun.dashspace.carrentalsystem.service.location.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @GetMapping("/provinces")
    public List<Province> getProvinces() {
        return locationService.getAllProvinces();
    }

    @GetMapping("/districts/{provinceCode}")
    public List<District> getDistrictsByCode(@PathVariable int provinceCode) {
        return locationService.getDistrictsByProvinceCodeAndDepth(provinceCode);
    }

    @GetMapping("/wards/{districtCode}")
    public List<Ward> getWardsByCode(@PathVariable int districtCode) {
        return locationService.getWardsByDistrictCodeAndDepth(districtCode);
    }
}
