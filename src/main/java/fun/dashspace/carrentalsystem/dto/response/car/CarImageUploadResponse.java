package fun.dashspace.carrentalsystem.dto.response.car;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CarImageUploadResponse {
    private Integer carId;
    private List<CarImageDTO> uploadedImages;
    private Integer totalImages;
    private String message;
}
