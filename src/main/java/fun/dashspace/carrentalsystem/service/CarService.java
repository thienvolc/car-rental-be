package fun.dashspace.carrentalsystem.service;

import fun.dashspace.carrentalsystem.dto.car.GetCarResponse;
import fun.dashspace.carrentalsystem.dto.car.PostCarRequest;

public interface CarService {
    void createCar(PostCarRequest req);

    void validateCarOwnerShip(Integer carId);
    GetCarResponse getCarDetails(Integer carId);
}
