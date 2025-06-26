package fun.dashspace.carrentalsystem.controller;

import fun.dashspace.carrentalsystem.dto.car.*;
import fun.dashspace.carrentalsystem.dto.common.response.ApiResponse;
import fun.dashspace.carrentalsystem.service.CarCertificateService;
import fun.dashspace.carrentalsystem.service.CarImageService;
import fun.dashspace.carrentalsystem.service.CarLocationService;
import fun.dashspace.carrentalsystem.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.IntStream;

@RestController
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;
    private final CarImageService carImageService;
    private final CarCertificateService carCertificateService;
    private final CarLocationService carLocationService;

    @PostMapping("/portal/cars")
    public ResponseEntity<ApiResponse<String>> postCar(@RequestBody PostCarRequest req) {
        carService.createCar(req);
        return ResponseEntity.ok(ApiResponse.ok("Post car successful"));
    }

    @PutMapping("/portal/cars/{carId}/rental")
    public ResponseEntity<ApiResponse<String>> updateRentalInfo(
            @PathVariable Integer carId, @RequestBody UpdateCarRentalInfoRequest req) {
        carService.validateCarOwnerShip(carId);
        carService.updateCarRentalInfo(carId, req);
        return ResponseEntity.ok(ApiResponse.ok("Update rental info successful"));
    }

    @PutMapping("/portal/cars/{carId}/location")
    public ResponseEntity<ApiResponse<String>> updateLocation(
            @PathVariable Integer carId, @RequestBody UpdateCarLocationRequest req) {
        carService.validateCarOwnerShip(carId);
        carLocationService.update(carId, req);
        return ResponseEntity.ok(ApiResponse.ok("Update car location successful"));
    }

    @PutMapping("/portal/cars/{carId}/images")
    public ResponseEntity<ApiResponse<String>> uploadCarImages(
            @PathVariable Integer carId,
            @RequestPart("orders") List<CarImageOrder> orders,
            @RequestPart("images") List<MultipartFile> images) {
        carService.validateCarOwnerShip(carId);
        List<UploadCarImageRequest> uploadImageReqList = toUploadCarImageRequestList(orders, images);
        carImageService.uploadCarImages(carId, uploadImageReqList);
        return ResponseEntity.ok(ApiResponse.ok("Update car images successful"));
    }

    private List<UploadCarImageRequest> toUploadCarImageRequestList(List<CarImageOrder> orders, List<MultipartFile> images) {
        if (orders.size() != images.size())
            throw new IllegalArgumentException("Orders and images size mismatch");

        return IntStream.range(0, orders.size())
                .mapToObj(i -> toUploadCarImageRequest(orders.get(i).getImageOrder(), images.get(i)))
                .toList();
    }

    private UploadCarImageRequest toUploadCarImageRequest(Integer order, MultipartFile image) {
        return UploadCarImageRequest.builder()
                .imageOrder(order)
                .carImage(image)
                .build();
    }

    @GetMapping("/portal/cars/{carId}")
    public ResponseEntity<ApiResponse<GetCarPortalResponse>> getCarPortalDetails(@PathVariable Integer carId) {
        carService.validateCarOwnerShip(carId);
        var carDetails = carService.getCarPortalDetails(carId);
        return ResponseEntity.ok(ApiResponse.ok(carDetails, "Get car details successful"));
    }

    @GetMapping("/portals/cars/all")
    public ResponseEntity<ApiResponse<GetAllCarsResponse>> getAllOwnedCars() {
        var carListRes = carService.getAllOwnedCars();
        return ResponseEntity.ok(ApiResponse.ok(carListRes, "Get all car successful"));
    }

    @GetMapping("/cars/all")
    public ResponseEntity<ApiResponse<GetAllCarsResponse>> getAllCars() {
        var carListRes = carService.getAllCars();
        return ResponseEntity.ok(ApiResponse.ok(carListRes, "Get all car successful"));
    }

    @GetMapping("/cars/{carId}/bookings")
    public ResponseEntity<ApiResponse<CarResponseDTO>> getCarDetails(@PathVariable Integer carId) {
        CarResponseDTO res = carService.getCarDetails(carId);
        return ResponseEntity.ok(ApiResponse.ok(res, "Get car successful"));
    }

    @PutMapping("/portal/cars/{carId}/certificate")
    public ResponseEntity<ApiResponse<String>> uploadCertificate(
            @PathVariable Integer carId,
            @ModelAttribute UpdateCarCertificateRequest req) {
        carService.validateCarOwnerShip(carId);
        carCertificateService.uploadCarCertificate(carId, req);
        return ResponseEntity.ok(ApiResponse.ok("Upload certificate successful"));
    }

    @PutMapping("/portal/cars/{carId}/status")
    public ResponseEntity<ApiResponse<String>> updateCarStatus(
            @PathVariable Integer carId, @RequestBody UpdateCarStatusRequest req) {
        carService.validateCarOwnerShip(carId);
        carService.updateCarStatus(carId, req);
        return ResponseEntity.ok(ApiResponse.ok("Update car status successful"));
    }
}
