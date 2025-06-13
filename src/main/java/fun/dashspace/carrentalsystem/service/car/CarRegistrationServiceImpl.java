package fun.dashspace.carrentalsystem.service.car;


import fun.dashspace.carrentalsystem.dto.request.car.CarRegistrationRequestDTO;
import fun.dashspace.carrentalsystem.dto.request.car.CarRegistrationService;
import fun.dashspace.carrentalsystem.dto.response.car.CarRegistrationResponse;
import fun.dashspace.carrentalsystem.entity.Car;
import fun.dashspace.carrentalsystem.entity.CarLocation;
import fun.dashspace.carrentalsystem.entity.User;
import fun.dashspace.carrentalsystem.enums.ApprovalStatus;
import fun.dashspace.carrentalsystem.enums.CarStatus;
import fun.dashspace.carrentalsystem.enums.FuelType;
import fun.dashspace.carrentalsystem.enums.TransmissionType;
import fun.dashspace.carrentalsystem.exception.custom.resource.ResourceNotFoundException;
import fun.dashspace.carrentalsystem.exception.custom.validation.ResourceAlreadyExistsException;
import fun.dashspace.carrentalsystem.repository.CarLocationRepository;
import fun.dashspace.carrentalsystem.repository.CarRepository;
import fun.dashspace.carrentalsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CarRegistrationServiceImpl implements CarRegistrationService {

    private final CarRepository carRepository;
    private final CarLocationRepository carLocationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CarRegistrationResponse registerCar(Integer ownerId, CarRegistrationRequestDTO request) {
        // 1. Check if license plate already exists
        if (carRepository.existsByLicensePlateNumber(request.getLicensePlateNumber())) {
            throw new ResourceAlreadyExistsException("Car with license plate " + request.getLicensePlateNumber() + " already exists");
        }

        // 2. Get car owner
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + ownerId));

        // 3. Create location
        CarLocation location = createCarLocation(owner, request);

        // 4. Create car
        Car car = createCar(owner, location, request);

        // 5. Build response
        return CarRegistrationResponse.builder()
                .carId(car.getId())
                .licensePlateNumber(car.getLicensePlateNumber())
                .brand(car.getBrand())
                .model(car.getModel())
                .message("Car registered successfully and pending approval")
                .build();
    }

    private CarLocation createCarLocation(User owner, CarRegistrationRequestDTO request) {
        CarLocation location = CarLocation.builder()
                .user(owner)
                .province(request.getLocation().getProvince())
                .district(request.getLocation().getDistrict())
                .ward(request.getLocation().getWard())
                .addressDetails(request.getLocation().getAddressDetails())
                .latitude(request.getLocation().getLatitude())
                .longitude(request.getLocation().getLongitude())
                .build();

        return carLocationRepository.save(location);
    }

    private Car createCar(User owner, CarLocation location, CarRegistrationRequestDTO request) {
        Car car = Car.builder()
                .owner(owner)
                .location(location)
                .licensePlateNumber(request.getLicensePlateNumber())
                .yearOfManufacture(request.getYearOfManufacture())
                .brand(request.getBrand())
                .model(request.getModel())
                .numberOfSeats(request.getNumberOfSeats())
                .fuelType(FuelType.valueOf(request.getFuelType().toLowerCase()))
                .transmissionType(TransmissionType.valueOf(request.getTransmissionType().toLowerCase()))
                .fuelConsumption(request.getFuelConsumption())
                .basePricePerDay(request.getBasePricePerDay())
                .description(request.getDescription())
                .approvalStatus(ApprovalStatus.pending)
                .status(CarStatus.active)
                .build();

        return carRepository.save(car);
    }
}