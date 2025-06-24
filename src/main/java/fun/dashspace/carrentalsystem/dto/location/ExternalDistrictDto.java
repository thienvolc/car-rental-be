package fun.dashspace.carrentalsystem.dto.location;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExternalDistrictDto {
    private String name;
    private Integer code;
    private String division_type;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ExternalWardDto> wards;
}
