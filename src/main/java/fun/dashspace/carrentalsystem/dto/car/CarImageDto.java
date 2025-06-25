package fun.dashspace.carrentalsystem.dto.car;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CarImageDto {
    private Integer imageOrder;
    private String imageUrl;
}
