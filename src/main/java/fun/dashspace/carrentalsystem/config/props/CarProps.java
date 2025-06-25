package fun.dashspace.carrentalsystem.config.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class CarProps {
    private Integer maxImagePerCar = 4;
}
