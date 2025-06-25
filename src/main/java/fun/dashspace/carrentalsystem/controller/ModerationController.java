package fun.dashspace.carrentalsystem.controller;

import fun.dashspace.carrentalsystem.dto.car.ReviewCarRequest;
import fun.dashspace.carrentalsystem.dto.common.response.ApiResponse;
import fun.dashspace.carrentalsystem.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ModerationController {

    private final CarService carService;

    @PutMapping("/cars/{carId}/review")
    public ResponseEntity<ApiResponse<String>> reviewCar(
            @PathVariable Integer carId, @RequestBody ReviewCarRequest req) {
        carService.updateCarApprovalStatus(carId, req);
        return ResponseEntity.ok(ApiResponse.ok("Review car successful"));
    }
}
