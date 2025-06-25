package fun.dashspace.carrentalsystem.service.impl;

import fun.dashspace.carrentalsystem.dto.car.CarLocationDto;
import fun.dashspace.carrentalsystem.entity.CarLocation;
import fun.dashspace.carrentalsystem.repository.CarLocationRepo;
import fun.dashspace.carrentalsystem.security.AuthenticateFacade;
import fun.dashspace.carrentalsystem.service.CarLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarLocationServiceImpl implements CarLocationService {

    private final CarLocationRepo carLocationRepo;
    private final AuthenticateFacade authenticateFacade;

    @Override
    public CarLocation createCarLocation(CarLocationDto location) {
        var carLocation = buildLocation(location);
        return carLocationRepo.save(carLocation);
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
}
