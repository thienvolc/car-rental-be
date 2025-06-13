package fun.dashspace.carrentalsystem.dto.response.car;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarImageDTO {
    private String imageUrl;
    private Integer order;
}
