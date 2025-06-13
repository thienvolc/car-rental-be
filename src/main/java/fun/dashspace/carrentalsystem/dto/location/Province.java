package fun.dashspace.carrentalsystem.dto.location;

import lombok.Data;

import java.util.List;

@Data
public class Province {
    private String name;
    private int code;
    private String division_type;
    private List<District> districts;
}