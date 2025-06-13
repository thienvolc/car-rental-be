package fun.dashspace.carrentalsystem.dto.response.car;

import lombok.Data;

@Data
public class CarOwnerDTO {
    private Integer id;
    private String username;
    private String avatarUrl;
    private String phone;
}
