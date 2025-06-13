package fun.dashspace.carrentalsystem.service.car;

import fun.dashspace.carrentalsystem.dto.response.car.CarDetailResponseDTO;
import fun.dashspace.carrentalsystem.dto.response.car.CarImageDTO;
import fun.dashspace.carrentalsystem.dto.response.car.CarOwnerDTO;
import fun.dashspace.carrentalsystem.dto.response.car.LocationDTO;
import fun.dashspace.carrentalsystem.entity.Car;
import fun.dashspace.carrentalsystem.entity.CarImage;
import fun.dashspace.carrentalsystem.entity.CarLocation;
import fun.dashspace.carrentalsystem.entity.User;
import fun.dashspace.carrentalsystem.repository.CarImageRepository;
import fun.dashspace.carrentalsystem.repository.CarLocationRepository;
import fun.dashspace.carrentalsystem.repository.CarRepository;
import fun.dashspace.carrentalsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarDetailServiceImpl {

    private final CarRepository carRepository;
    private final CarImageRepository carImageRepository;
    private final CarLocationRepository carLocationRepository;
    private final UserRepository userRepository;

    public CarDetailResponseDTO getCarDetail(Integer carId) {
        Optional<Car> carOptional = carRepository.findById(carId);

        if (carOptional.isEmpty()) {
            return null;
        }

        Car car = carOptional.get();

        // Get car images
        List<CarImage> carImages = carImageRepository.findByCarIdOrderByImageOrder(carId);

        // Get car location
        Optional<CarLocation> locationOptional = carLocationRepository.findById(car.getLocation().getId());
        if (locationOptional.isEmpty()) {
            return null;
        }
        CarLocation location = locationOptional.get();

        // Get car owner information
        Optional<User> ownerOptional = userRepository.findById(car.getOwner().getId());
        if (ownerOptional.isEmpty()) {
            return null;
        }
        User owner = ownerOptional.get();

        // Build and return the response DTO
        return buildCarDetailResponse(car, carImages, location, owner);
    }

    private CarDetailResponseDTO buildCarDetailResponse(Car car, List<CarImage> images,
                                                        CarLocation location, User owner) {
        CarDetailResponseDTO responseDTO = new CarDetailResponseDTO();

        // Set car details
        responseDTO.setId(car.getId());
        responseDTO.setBrand(car.getBrand());
        responseDTO.setModel(car.getModel());
        responseDTO.setLicensePlateNumber(car.getLicensePlateNumber());
        responseDTO.setYearOfManufacture(car.getYearOfManufacture());
        responseDTO.setNumberOfSeats(car.getNumberOfSeats());
        responseDTO.setFuelType(car.getFuelType().toString());
        responseDTO.setTransmissionType(car.getTransmissionType().toString());
        responseDTO.setFuelConsumption(car.getFuelConsumption());
        responseDTO.setBasePricePerDay(car.getBasePricePerDay());
        responseDTO.setDescription(car.getDescription());

        // Set images
        List<CarImageDTO> imagesList = images.stream()
                .map(image -> CarImageDTO.builder()
                        .imageUrl(image.getImageUrl())
                        .order(image.getImageOrder())
                        .build())
                .collect(Collectors.toList());
        responseDTO.setImages(imagesList);

        // Set location
        var locationDTO = new LocationDTO();
        locationDTO.setAddressDetails(location.getAddressDetails());
        locationDTO.setProvince(location.getProvince());
        locationDTO.setDistrict(location.getDistrict());
        locationDTO.setWard(location.getWard());

        responseDTO.setLocation(locationDTO);

        // Set owner info
        CarOwnerDTO ownerDTO = new CarOwnerDTO();
        ownerDTO.setId(owner.getId());
        ownerDTO.setUsername(owner.getUsername());
        ownerDTO.setAvatarUrl(owner.getAvatarUrl());
        ownerDTO.setPhone(owner.getPhoneNumber());
        responseDTO.setOwner(ownerDTO);

        return responseDTO;
    }
}