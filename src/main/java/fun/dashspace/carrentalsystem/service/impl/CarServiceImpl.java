package fun.dashspace.carrentalsystem.service.impl;

import fun.dashspace.carrentalsystem.dto.car.PostCarRequest;
import fun.dashspace.carrentalsystem.entity.Car;
import fun.dashspace.carrentalsystem.entity.CarLocation;
import fun.dashspace.carrentalsystem.exception.custom.resource.ResourceNotFoundException;
import fun.dashspace.carrentalsystem.repository.CarRepo;
import fun.dashspace.carrentalsystem.security.AuthenticateFacade;
import fun.dashspace.carrentalsystem.service.CarLocationService;
import fun.dashspace.carrentalsystem.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarLocationService carLocationService;
    private final AuthenticateFacade authenticateFacade;
    private final CarRepo carRepo;

    @Override
    public void createCar(PostCarRequest req) {
        var location = carLocationService.createCarLocation(req.getLocation());
        Car car = buildCar(req, location);
        carRepo.save(car);
    }

    private Car buildCar(PostCarRequest req, CarLocation location) {
        return Car.builder()
                .owner(authenticateFacade.getCurrentUser())
                .brand(req.getBrand())
                .model(req.getModel())
                .yearOfManufacture(req.getYearOfManufacture())
                .basePricePerDay(req.getBasePricePerDay())
                .description(req.getDescription())
                .location(location)
                .licensePlateNumber(req.getLicensePlateNumber())
                .fuelConsumption(req.getFuelConsumption())
                .fuelType(req.getFuelType())
                .numberOfSeats(req.getNumberOfSeats())
                .transmissionType(req.getTransmissionType())
                .build();
    }

    @Override
    public Car getCarOrThrow(Integer carId) {
        return carRepo.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + carId));
    }
}
