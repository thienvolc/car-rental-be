package fun.dashspace.carrentalsystem.dto.car;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
public class UploadCarImageRequest {
    private Integer imageOrder;
    private MultipartFile carImage;
}
