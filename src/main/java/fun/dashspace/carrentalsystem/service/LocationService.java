package fun.dashspace.carrentalsystem.service;

import fun.dashspace.carrentalsystem.dto.location.DistrictListResponse;
import fun.dashspace.carrentalsystem.dto.location.ProvinceListResponse;
import fun.dashspace.carrentalsystem.dto.location.WardListResponse;

public interface LocationService {
    ProvinceListResponse getAllProvinces();
    DistrictListResponse getDistrictsByProvinceCodeAndDepth(int provinceCode);
    WardListResponse getWardsByDistrictCodeAndDepth(int districtCode);
}
