package fun.dashspace.carrentalsystem.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetAllUsersReponse {
    List<UserDisplayItem> users;
}
