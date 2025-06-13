package fun.dashspace.carrentalsystem.service.car;

import fun.dashspace.carrentalsystem.dto.response.car.CarImageDTO;
import fun.dashspace.carrentalsystem.dto.response.car.CarImageUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CarImageService {
    CarImageUploadResponse uploadCarImages(Integer userId, Integer carId, List<MultipartFile> images);

    List<CarImageDTO> getCarImages(Integer carId);
}