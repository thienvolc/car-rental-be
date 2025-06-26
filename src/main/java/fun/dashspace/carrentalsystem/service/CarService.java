package fun.dashspace.carrentalsystem.service;

import fun.dashspace.carrentalsystem.dto.car.*;

public interface CarService {
    void createCar(PostCarRequest req);

    void validateCarOwnerShip(Integer carId);
    GetCarResponse getCarDetails(Integer carId);

    void updateCarStatus(Integer carId, UpdateCarStatusRequest req);

    void updateCarApprovalStatus(Integer carId, ReviewCarRequest req);

    void updateCarRentalInfo(Integer carId, UpdateCarRentalInfoRequest req);

    GetAllCarsResponse getAllOwnedCars();

    GetAllCarsResponse getAllCars();
}
