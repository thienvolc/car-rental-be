package fun.dashspace.carrentalsystem.dto.response.auth;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserInfo {
    private Integer id;
    private String username;
    private String email;
    private List<String> roles;
}
