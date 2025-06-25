package fun.dashspace.carrentalsystem.dto.car;

import fun.dashspace.carrentalsystem.enums.CarStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCarStatusRequest {
    private CarStatus status;
}
