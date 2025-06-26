package fun.dashspace.carrentalsystem.controller;

import fun.dashspace.carrentalsystem.dto.car.ReviewCarRequest;
import fun.dashspace.carrentalsystem.dto.common.response.ApiResponse;
import fun.dashspace.carrentalsystem.dto.user.GetAllUsersReponse;
import fun.dashspace.carrentalsystem.dto.user.UpdateUserStatusRequest;
import fun.dashspace.carrentalsystem.service.AuthService;
import fun.dashspace.carrentalsystem.service.CarService;
import fun.dashspace.carrentalsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ModerationController {

    private final CarService carService;
    private final UserService userService;

    @PutMapping("/cars/{carId}/review")
    public ResponseEntity<ApiResponse<String>> reviewCar(
            @PathVariable Integer carId, @RequestBody ReviewCarRequest req) {
        carService.updateCarApprovalStatus(carId, req);
        return ResponseEntity.ok(ApiResponse.ok("Review car successful"));
    }

    @PutMapping("/users/{userId}/status")
    public ResponseEntity<ApiResponse<String>> updateUserStatus(
            @PathVariable Integer userId, @RequestBody UpdateUserStatusRequest req) {
        userService.updateUserStatus(userId, req);
        return ResponseEntity.ok(ApiResponse.ok("Update user status successful"));
    }

    @GetMapping("/users/all")
    public ResponseEntity<ApiResponse<GetAllUsersReponse>> getAllUsers() {
        var userListRes = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.ok(userListRes, "Get all user successful"));
    }
}
