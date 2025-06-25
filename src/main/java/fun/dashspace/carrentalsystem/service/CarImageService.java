package fun.dashspace.carrentalsystem.service;

import fun.dashspace.carrentalsystem.dto.car.UploadCarImageRequest;

import java.util.List;

public interface CarImageService {
    void uploadCarImages(Integer carId, List<UploadCarImageRequest> uploadImageReqList);
}
