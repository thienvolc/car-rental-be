package fun.dashspace.carrentalsystem.service.impl;

import fun.dashspace.carrentalsystem.config.props.CarProps;
import fun.dashspace.carrentalsystem.dto.car.UploadCarImageRequest;
import fun.dashspace.carrentalsystem.entity.Car;
import fun.dashspace.carrentalsystem.entity.CarImage;
import fun.dashspace.carrentalsystem.exception.custom.resource.ResourceNotFoundException;
import fun.dashspace.carrentalsystem.repository.CarImageRepo;
import fun.dashspace.carrentalsystem.repository.CarRepo;
import fun.dashspace.carrentalsystem.service.CarImageService;
import fun.dashspace.carrentalsystem.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarImageServiceImpl implements CarImageService {

    private final CarImageRepo carImageRepo;
    private final CarRepo carRepo;
    private final ImageUploadService imageUploadService;
    private final CarProps carProps;

    @Override
    @Transactional
    public void uploadCarImages(Integer carId, List<UploadCarImageRequest> uploadImageReqList) {
        validateNotOutOfImageLimitPerCar(uploadImageReqList.size());
        var car = getCarOrThrow(carId);
        for (var req : uploadImageReqList)
            updateOrCreateCarImage(car, req);
    }

    private Car getCarOrThrow(Integer carId) {
        return carRepo.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + carId));
    }

    private void validateNotOutOfImageLimitPerCar(int imageCount) {
        if (imageCount > carProps.getMaxImagePerCar())
            throw new IllegalArgumentException("Cannot upload more than " + carProps.getMaxImagePerCar() + " images per car");
    }

    private void updateOrCreateCarImage(Car car, UploadCarImageRequest req) {
        var imageUrl = imageUploadService.uploadFile(req.getCarImage()).getSecureUrl();
        Integer imageOrder = req.getImageOrder();
        Optional<CarImage> existingImage = carImageRepo.findByCarIdAndImageOrder(car.getId(), imageOrder);

        if (existingImage.isPresent())
            updateExistingCarImage(existingImage.get(), imageUrl);
        else
            createNewCarImage(car, imageUrl, imageOrder);
    }

    private void updateExistingCarImage(CarImage carImage, String imageUrl) {
        imageUploadService.deleteFile(carImage.getImageUrl());
        carImage.setImageUrl(imageUrl);
        carImageRepo.save(carImage);
    }

    private void createNewCarImage(Car car, String imageUrl, Integer imageOrder) {
        CarImage newCarImage = CarImage.builder()
                .car(car)
                .imageUrl(imageUrl)
                .imageOrder(imageOrder)
                .build();
        carImageRepo.save(newCarImage);
    }
}
