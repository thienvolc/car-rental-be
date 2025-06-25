package fun.dashspace.carrentalsystem.service;

import fun.dashspace.carrentalsystem.dto.car.GetCarResponse;
import fun.dashspace.carrentalsystem.dto.car.PostCarRequest;
import fun.dashspace.carrentalsystem.dto.car.ReviewCarRequest;
import fun.dashspace.carrentalsystem.dto.car.UpdateCarStatusRequest;

public interface CarService {
    void createCar(PostCarRequest req);

    void validateCarOwnerShip(Integer carId);
    GetCarResponse getCarDetails(Integer carId);

    void updateCarStatus(Integer carId, UpdateCarStatusRequest req);

    void updateCarApprovalStatus(Integer carId, ReviewCarRequest req);
}
