package fun.dashspace.carrentalsystem.dto.location;

import lombok.Data;

import java.util.List;

@Data
public class District {
    private String name;
    private int code;
    private String division_type;
    private List<Ward> wards;
}
