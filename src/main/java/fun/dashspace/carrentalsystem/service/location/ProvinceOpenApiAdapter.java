package fun.dashspace.carrentalsystem.service.location;

import fun.dashspace.carrentalsystem.dto.location.District;
import fun.dashspace.carrentalsystem.dto.location.Province;
import fun.dashspace.carrentalsystem.dto.location.Ward;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProvinceOpenApiAdapter implements LocationService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "https://provinces.open-api.vn/api";

    @Override
    public List<Province> getAllProvinces() {
        ResponseEntity<List<Province>> res = restTemplate.exchange(
                BASE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<Province>>() {});
        return res.getBody();
    }

    @Override
    public List<District> getDistrictsByProvinceCodeAndDepth(int provinceCode) {
        Province province = restTemplate.getForObject(
                BASE_URL + "/p/" + provinceCode + "?depth=" + 2, Province.class);
        if (province == null) {
            return Collections.emptyList();
        }
        return province.getDistricts();
    }

    @Override
    public List<Ward> getWardsByDistrictCodeAndDepth(int districtCode) {
        District district = restTemplate.getForObject(
                BASE_URL + "/d/" + districtCode + "?depth=" + 2, District.class);
        if (district == null) {
            return Collections.emptyList();
        }
        return district.getWards();
    }
}
