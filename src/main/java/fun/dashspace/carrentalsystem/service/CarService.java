package fun.dashspace.carrentalsystem.service;

import fun.dashspace.carrentalsystem.dto.car.PostCarRequest;
import fun.dashspace.carrentalsystem.entity.Car;

public interface CarService {
    void createCar(PostCarRequest req);

    Car getCarOrThrow(Integer carId);
}
