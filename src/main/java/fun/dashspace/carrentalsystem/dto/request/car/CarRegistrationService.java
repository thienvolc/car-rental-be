package fun.dashspace.carrentalsystem.dto.request.car;

import fun.dashspace.carrentalsystem.dto.response.car.CarRegistrationResponse;

public interface CarRegistrationService {
    CarRegistrationResponse registerCar(Integer ownerId, CarRegistrationRequestDTO request);
}
