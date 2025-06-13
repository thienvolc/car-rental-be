package fun.dashspace.carrentalsystem.service.car;

import fun.dashspace.carrentalsystem.dto.response.car.CarImageDTO;
import fun.dashspace.carrentalsystem.dto.response.car.CarImageUploadResponse;
import fun.dashspace.carrentalsystem.dto.response.upload.FileUpLoadResponse;
import fun.dashspace.carrentalsystem.entity.Car;
import fun.dashspace.carrentalsystem.entity.CarImage;
import fun.dashspace.carrentalsystem.exception.custom.auth.ForbiddenException;
import fun.dashspace.carrentalsystem.exception.custom.resource.ResourceNotFoundException;
import fun.dashspace.carrentalsystem.exception.custom.validation.BadRequestException;
import fun.dashspace.carrentalsystem.repository.CarImageRepository;
import fun.dashspace.carrentalsystem.repository.CarRepository;
import fun.dashspace.carrentalsystem.upload.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarImageServiceImpl implements CarImageService {

    private final CarRepository carRepository;
    private final CarImageRepository carImageRepository;
    private final FileUploadService fileUploadService;

    @Value("${app.upload.car-images.max-count:20}")
    private int maxImagesPerCar;

    @Value("${app.upload.car-images.folder:cars}")
    private String uploadFolder;

    @Override
    @Transactional
    public CarImageUploadResponse uploadCarImages(Integer userId, Integer carId, List<MultipartFile> images) {
        // 1. Find car and validate ownership
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + carId));

        if (!Objects.equals(car.getOwner().getId(), userId)) {
            throw new ForbiddenException("You don't have permission to upload images for this car");
        }

        // 2. Get existing images and check if adding more would exceed limit
        List<CarImage> existingImages = carImageRepository.findByCarIdOrderByImageOrder(carId);
        if (existingImages.size() + images.size() > maxImagesPerCar) {
            throw new BadRequestException("Maximum " + maxImagesPerCar + " images allowed per car. " +
                    "You already have " + existingImages.size() + " images.");
        }

        // 3. Upload images to Cloudinary
        List<FileUpLoadResponse> uploadResponses = fileUploadService.uploadFiles(images, uploadFolder + "/" + carId);

        // 4. Save image records to database
        List<CarImage> savedImages = new ArrayList<>();
        int nextOrder = existingImages.isEmpty() ? 1 : existingImages.stream()
                .mapToInt(CarImage::getImageOrder)
                .max()
                .orElse(0) + 1;

        for (FileUpLoadResponse uploadResponse : uploadResponses) {
            CarImage carImage = CarImage.builder()
                    .car(car)
                    .imageOrder(nextOrder++)
                    .imageUrl(uploadResponse.getSecureUrl())
                    .build();
            savedImages.add(carImageRepository.save(carImage));
        }

        // 5. Build response
        List<CarImageDTO> uploadedImageDTOs = savedImages.stream()
                .map(image -> CarImageDTO.builder()
                        .imageUrl(image.getImageUrl())
                        .order(image.getImageOrder())
                        .build())
                .collect(Collectors.toList());

        return CarImageUploadResponse.builder()
                .carId(carId)
                .uploadedImages(uploadedImageDTOs)
                .totalImages(existingImages.size() + uploadedImageDTOs.size())
                .message("Images uploaded successfully")
                .build();
    }

    @Override
    public List<CarImageDTO> getCarImages(Integer carId) {
        // Check if car exists
        if (!carRepository.existsById(carId)) {
            throw new ResourceNotFoundException("Car not found with id: " + carId);
        }

        // Get and convert images to DTOs
        return carImageRepository.findByCarIdOrderByImageOrder(carId).stream()
                .map(image -> CarImageDTO.builder()
                        .imageUrl(image.getImageUrl())
                        .order(image.getImageOrder())
                        .build())
                .collect(Collectors.toList());
    }
}