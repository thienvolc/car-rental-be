package fun.dashspace.carrentalsystem.dto.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class WardListResponse {
    private List<ExternalWardDto> wards;
}
