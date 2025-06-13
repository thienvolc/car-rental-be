package fun.dashspace.carrentalsystem.service.location;

import fun.dashspace.carrentalsystem.dto.location.District;
import fun.dashspace.carrentalsystem.dto.location.Province;
import fun.dashspace.carrentalsystem.dto.location.Ward;

import java.util.List;

public interface LocationService {

    List<Province> getAllProvinces();
    List<District> getDistrictsByProvinceCodeAndDepth(int provinceCode);
    List<Ward> getWardsByDistrictCodeAndDepth(int districtCode);
}
