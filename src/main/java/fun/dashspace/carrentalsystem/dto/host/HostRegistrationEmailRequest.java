package fun.dashspace.carrentalsystem.dto.host;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HostRegistrationEmailRequest {
    private String email;
    private Integer userId;
}
