package fun.dashspace.carrentalsystem.external;

import fun.dashspace.carrentalsystem.dto.location.ExternalDistrictDto;
import fun.dashspace.carrentalsystem.dto.location.ExternalProvinceDto;
import fun.dashspace.carrentalsystem.dto.location.ExternalWardDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class VietNamProvincesOpenApiAdapter {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "https://provinces.open-api.vn/api";

    public List<ExternalProvinceDto> findAllProvinces() {
        try {
            ResponseEntity<List<ExternalProvinceDto>> res = restTemplate.exchange(
                    BASE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
            return Optional.ofNullable(res.getBody()).orElse(Collections.emptyList());
        } catch (RestClientException ex) {
            return Collections.emptyList();
        }
    }

    public List<ExternalDistrictDto> findDistrictsByProvinceCodeAndDepth(
            int provinceCode) throws RestClientException {
        try {
            ExternalProvinceDto province = restTemplate.getForObject(
                    BASE_URL + "/p/" + provinceCode + "?depth=" + 2, ExternalProvinceDto.class);
            return Optional.ofNullable(province)
                    .map(ExternalProvinceDto::getDistricts)
                    .orElse(Collections.emptyList());
        } catch (RestClientException ex) {
            return Collections.emptyList();
        }
    }

    public List<ExternalWardDto> findWardsByDistrictCodeAndDepth(
            int districtCode) throws RestClientException {
        try {
            ExternalDistrictDto district = restTemplate.getForObject(
                    BASE_URL + "/d/" + districtCode + "?depth=" + 2, ExternalDistrictDto.class);
            return Optional.ofNullable(district)
                    .map(ExternalDistrictDto::getWards)
                    .orElse(Collections.emptyList());
        } catch (RestClientException ex) {
            return Collections.emptyList();
        }
    }
}
