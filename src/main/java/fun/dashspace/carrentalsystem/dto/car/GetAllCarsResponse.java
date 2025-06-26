package fun.dashspace.carrentalsystem.dto.car;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetAllCarsResponse {
    private List<CarDisplayItem> cars;
}
