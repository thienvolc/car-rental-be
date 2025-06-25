package fun.dashspace.carrentalsystem.controller;

import fun.dashspace.carrentalsystem.dto.common.response.ApiResponse;
import fun.dashspace.carrentalsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

//    @GetMapping("/info")
//    public ResponseEntity<ApiResponse<UserIdenti>> getHostRegistrationInfo() {
//        var hostRegistraionInfo = userIdentificationService.getHostRegistraionInfo();
//        return ResponseEntity.ok(ApiResponse.ok(hostRegistraionInfo, "Host registration info retrieved successfully"));
//    }


}
