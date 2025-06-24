package fun.dashspace.carrentalsystem.dto.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProvinceListResponse {
    private List<ExternalProvinceDto> provinces;
}
