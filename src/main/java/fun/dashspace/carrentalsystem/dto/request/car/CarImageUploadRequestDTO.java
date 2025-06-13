package fun.dashspace.carrentalsystem.dto.request.car;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CarImageUploadRequestDTO {
    @NotNull(message = "Car ID is required")
    private Integer carId;

    @NotNull(message = "Images are required")
    private List<MultipartFile> images;
}