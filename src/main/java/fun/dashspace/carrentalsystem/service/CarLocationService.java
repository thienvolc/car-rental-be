package fun.dashspace.carrentalsystem.service;

import fun.dashspace.carrentalsystem.dto.car.CarLocationDto;
import fun.dashspace.carrentalsystem.dto.car.UpdateCarLocationRequest;
import fun.dashspace.carrentalsystem.entity.CarLocation;

public interface CarLocationService {
    CarLocation createCarLocation(CarLocationDto location);

    void update(Integer carId, UpdateCarLocationRequest carLocationDto);
}
