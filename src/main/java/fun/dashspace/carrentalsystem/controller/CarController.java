package fun.dashspace.carrentalsystem.controller;

import fun.dashspace.carrentalsystem.dto.car.CarImageOrder;
import fun.dashspace.carrentalsystem.dto.car.PostCarRequest;
import fun.dashspace.carrentalsystem.dto.car.UploadCarImageRequest;
import fun.dashspace.carrentalsystem.dto.common.response.ApiResponse;
import fun.dashspace.carrentalsystem.service.CarImageService;
import fun.dashspace.carrentalsystem.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/portal/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;
    private final CarImageService carImageService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> postCar(@RequestBody PostCarRequest req) {
        carService.createCar(req);
        return ResponseEntity.ok(ApiResponse.ok("Post car successful"));
    }

    @PutMapping("/{carId}/images")
    public ResponseEntity<ApiResponse<String>> uploadCarImages(
            @PathVariable Integer carId,
            @RequestPart("orders") List<CarImageOrder> orders,
            @RequestPart("images") List<MultipartFile> images) {
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
}
