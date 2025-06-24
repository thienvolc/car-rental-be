package fun.dashspace.carrentalsystem.dto.upload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FileUpLoadResponse {
    private String publicId;
    private String secureUrl;
}
