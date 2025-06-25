package fun.dashspace.carrentalsystem.controller;

import fun.dashspace.carrentalsystem.dto.common.response.ApiResponse;
import fun.dashspace.carrentalsystem.dto.user.UpdateUserProfileRequest;
import fun.dashspace.carrentalsystem.dto.user.GetUserProfileResponse;
import fun.dashspace.carrentalsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<GetUserProfileResponse>> getUserProfile() {
        var res = userService.getCurrentUserInfo();
        return ResponseEntity.ok(ApiResponse.ok(res, "User profile retrieved successfully"));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<String>> updateUserProfile(UpdateUserProfileRequest req) {
        userService.updateCurrentUserInfo(req);
        return ResponseEntity.ok(ApiResponse.ok("Update user profile successfully"));
    }

    @PutMapping("/profile/avatar")
    public ResponseEntity<ApiResponse<String>> updateUserProfileAvatar(
            @RequestParam("avatarImage") MultipartFile avatarImage) {
        userService.updateUserProfileAvatar(avatarImage);
        return ResponseEntity.ok(ApiResponse.ok("Update user profile avatar successfully"));
    }
}
