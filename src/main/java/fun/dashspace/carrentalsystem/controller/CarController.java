package fun.dashspace.carrentalsystem.controller;

import fun.dashspace.carrentalsystem.dto.request.car.CarRegistrationRequestDTO;
import fun.dashspace.carrentalsystem.dto.response.car.CarDetailResponseDTO;
import fun.dashspace.carrentalsystem.dto.response.car.CarImageDTO;
import fun.dashspace.carrentalsystem.dto.response.car.CarImageUploadResponse;
import fun.dashspace.carrentalsystem.dto.response.car.CarRegistrationResponse;
import fun.dashspace.carrentalsystem.dto.response.common.ApiResponse;
import fun.dashspace.carrentalsystem.security.CustomUserDetails;
import fun.dashspace.carrentalsystem.service.car.CarDetailServiceImpl;
import fun.dashspace.carrentalsystem.service.car.CarImageService;
import fun.dashspace.carrentalsystem.service.car.CarRegistrationServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarRegistrationServiceImpl carRegistrationService;
    private final CarDetailServiceImpl carDetailServiceImpl;
    private final CarImageService carImageService;

    @PostMapping
    public ResponseEntity<ApiResponse<CarRegistrationResponse>> registerCar(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody CarRegistrationRequestDTO request) {

        CarRegistrationResponse response = carRegistrationService.registerCar(currentUser.getId(), request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<CarRegistrationResponse>builder()
                        .success(true)
                        .message("Car registered successfully and pending approval")
                        .data(response)
                        .build());
    }

    @GetMapping("/{carId}")
    public ResponseEntity<CarDetailResponseDTO> getCarDetail(@PathVariable Integer carId) {
        CarDetailResponseDTO carDetail = carDetailServiceImpl.getCarDetail(carId);

        if (carDetail == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(carDetail);
    }

    @PostMapping(value = "/{carId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<CarImageUploadResponse>> uploadCarImages(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Integer carId,
            @RequestParam("images") List<MultipartFile> images) {

        CarImageUploadResponse response = carImageService.uploadCarImages(
                currentUser.getId(), carId, images);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<CarImageUploadResponse>builder()
                        .success(true)
                        .message("Car images uploaded successfully")
                        .data(response)
                        .build());
    }

    @GetMapping("/{carId}/images")
    public ResponseEntity<ApiResponse<List<CarImageDTO>>> getCarImages(
            @PathVariable Integer carId) {

        List<CarImageDTO> images = carImageService.getCarImages(carId);

        return ResponseEntity.ok(ApiResponse.<List<CarImageDTO>>builder()
                .success(true)
                .message("Car images retrieved successfully")
                .data(images)
                .build());
    }
}
