package fun.dashspace.carrentalsystem.service.impl;

import fun.dashspace.carrentalsystem.dto.car.CarLocationDto;
import fun.dashspace.carrentalsystem.dto.car.UpdateCarLocationRequest;
import fun.dashspace.carrentalsystem.entity.Car;
import fun.dashspace.carrentalsystem.entity.CarLocation;
import fun.dashspace.carrentalsystem.repository.CarLocationRepo;
import fun.dashspace.carrentalsystem.repository.CarRepo;
import fun.dashspace.carrentalsystem.security.AuthenticateFacade;
import fun.dashspace.carrentalsystem.service.CarLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CarLocationServiceImpl implements CarLocationService {

    private final CarLocationRepo carLocationRepo;
    private final AuthenticateFacade authenticateFacade;
    private final CarRepo carRepo;

    @Override
    public CarLocation createCarLocation(CarLocationDto location) {
        return carLocationRepo.save(buildLocation(location));
    }

    private CarLocation buildLocation(CarLocationDto location) {
        return CarLocation.builder()
                .user(authenticateFacade.getCurrentUser())
                .province(location.getProvince())
                .district(location.getDistrict())
                .ward(location.getWard())
                .addressDetails(location.getAddressDetails())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
    }

    @Override
    @Transactional
    public void update(Integer carId, UpdateCarLocationRequest req) {
        var car = getCarByIdOrThrow(carId);
        var location = car.getLocation();
        setLocationDetails(location, req);
        carLocationRepo.save(location);
    }

    private void setLocationDetails(CarLocation location, UpdateCarLocationRequest carLocationDto) {
        location.setProvince(carLocationDto.getProvince());
        location.setDistrict(carLocationDto.getDistrict());
        location.setWard(carLocationDto.getWard());
        location.setAddressDetails(carLocationDto.getAddressDetails());
        location.setLatitude(carLocationDto.getLatitude());
        location.setLongitude(carLocationDto.getLongitude());
    }

    private Car getCarByIdOrThrow(Integer carId) {
        return carRepo.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Car not found with id: " + carId));
    }
}
