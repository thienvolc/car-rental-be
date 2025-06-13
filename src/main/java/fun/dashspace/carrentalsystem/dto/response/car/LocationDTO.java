package fun.dashspace.carrentalsystem.dto.response.car;

import lombok.Data;

@Data
public class LocationDTO {
    private String province;
    private String district;
    private String ward;
    private String addressDetails;
}