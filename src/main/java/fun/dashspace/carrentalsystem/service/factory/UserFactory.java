package fun.dashspace.carrentalsystem.service.factory;

import fun.dashspace.carrentalsystem.entity.User;
import fun.dashspace.carrentalsystem.service.RoleService;
import fun.dashspace.carrentalsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFactory {
    private final UserService userService;
    private final RoleService roleService;

    public void createRenter(String email, String password) {
        User user = userService.createUser(email, password);
        roleService.assignRenterRoleToUser(user.getId());
    }
}