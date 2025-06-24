package fun.dashspace.carrentalsystem.service.impl;

import fun.dashspace.carrentalsystem.dto.location.*;
import fun.dashspace.carrentalsystem.exception.custom.resource.ResourceNotFoundException;
import fun.dashspace.carrentalsystem.external.VietNamProvincesOpenApiAdapter;
import fun.dashspace.carrentalsystem.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final VietNamProvincesOpenApiAdapter vietNamProvincesOpenApiAdapter;

    @Override
    public ProvinceListResponse getAllProvinces() {
        List<ExternalProvinceDto> provinces = vietNamProvincesOpenApiAdapter.findAllProvinces();
        if (provinces.isEmpty())
            throw new ResourceNotFoundException("Cannot find all provinces");
        return new ProvinceListResponse(provinces);
    }

    @Override
    public DistrictListResponse getDistrictsByProvinceCodeAndDepth(int provinceCode) {
        List<ExternalDistrictDto> districts = vietNamProvincesOpenApiAdapter.findDistrictsByProvinceCodeAndDepth(provinceCode);
        if (districts.isEmpty())
            throw new ResourceNotFoundException("Province code not found");
        return new DistrictListResponse(districts);
    }

    @Override
    public WardListResponse getWardsByDistrictCodeAndDepth(int districtCode) {
        List<ExternalWardDto> wards = vietNamProvincesOpenApiAdapter.findWardsByDistrictCodeAndDepth(districtCode);
        if (wards.isEmpty())
            throw new ResourceNotFoundException("District code not found");
        return new WardListResponse(wards);
    }
}
