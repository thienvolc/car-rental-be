package fun.dashspace.carrentalsystem.dto.user;

import fun.dashspace.carrentalsystem.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserStatusRequest {
    private UserStatus status;
}
