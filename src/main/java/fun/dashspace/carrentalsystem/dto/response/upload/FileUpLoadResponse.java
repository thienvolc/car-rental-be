package fun.dashspace.carrentalsystem.dto.response.upload;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FileUpLoadResponse {
    private String publicId;
    private String secureUrl;
}
