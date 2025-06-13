package fun.dashspace.carrentalsystem.service.car;

import fun.dashspace.carrentalsystem.dto.request.car.CarFilterDTO;
import fun.dashspace.carrentalsystem.dto.request.car.CarSearchRequestDTO;
import fun.dashspace.carrentalsystem.dto.response.car.CarImageDTO;
import fun.dashspace.carrentalsystem.dto.response.car.CarResponseDTO;
import fun.dashspace.carrentalsystem.dto.response.car.LocationDTO;
import fun.dashspace.carrentalsystem.entity.Car;
import fun.dashspace.carrentalsystem.entity.CarImage;
import fun.dashspace.carrentalsystem.entity.CarLocation;
import fun.dashspace.carrentalsystem.entity.Trip;
import fun.dashspace.carrentalsystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarSearchServiceImp {

    private final CarLocationRepository carLocationRepository;
    private final CarRepository carRepository;
    private final TripRepository tripRepository;
    private final CarImageRepository carImageRepository;
    private final UserRepository userRepository;

    public List<CarResponseDTO> searchCars(CarSearchRequestDTO searchRequest) {
        // 1. Find locations matching the search criteria
        List<CarLocation> locations = carLocationRepository.findByLocationContaining(searchRequest.getLocation());

        if (locations.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. Get location IDs
        List<Integer> locationIds = locations.stream()
                .map(CarLocation::getId)
                .collect(Collectors.toList());

        // 3. Find cars in these locations
        List<Car> cars = carRepository.findByLocationIds(locationIds);

        // 4. Create a map of location ID to location for faster lookup
        Map<Integer, CarLocation> locationMap = locations.stream()
                .collect(Collectors.toMap(CarLocation::getId, location -> location));

        // 5. Filter out unavailable cars based on trip schedules
        LocalDateTime pickupDateTime = LocalDateTime.of(
                searchRequest.getPickupDate(),
                searchRequest.getPickupTime()
        );

        LocalDateTime returnDateTime = LocalDateTime.of(
                searchRequest.getReturnDate(),
                searchRequest.getReturnTime()
        );

        // Calculate rental duration in days
        long rentalDays = ChronoUnit.DAYS.between(pickupDateTime.toLocalDate(), returnDateTime.toLocalDate());
        if (rentalDays < 1) rentalDays = 1; // Minimum 1 day

        List<CarResponseDTO> availableCars = new ArrayList<>();

        for (Car car : cars) {
            // Check if car has any overlapping trips
            List<Trip> overlappingTrips = tripRepository.findOverlappingTrips(
                    car.getId(), pickupDateTime, returnDateTime);

            // If car is rented but will be available by the requested time, or is active with no overlapping trips
            if (overlappingTrips.isEmpty()) {
                CarLocation carLocation = locationMap.get(car.getLocation().getId());
                List<CarImage> carImages = carImageRepository.findByCarIdOrderByImageOrder(car.getId());

                CarResponseDTO carResponse = mapToCarResponseDTO(car, carLocation, carImages);

                // Calculate total price based on rental days
                BigDecimal totalPrice = car.getBasePricePerDay().multiply(BigDecimal.valueOf(rentalDays));
                carResponse.setTotalPrice(totalPrice);

                availableCars.add(carResponse);
            }
        }

        return availableCars;
    }

    public List<CarResponseDTO> filterCars(List<CarResponseDTO> cars, CarFilterDTO filterDTO) {
        return cars.stream()
                .filter(car -> filterDTO.getBrand() == null || car.getBrand().equals(filterDTO.getBrand()))
                .filter(car -> filterDTO.getSeats() == null || car.getNumberOfSeats().equals(filterDTO.getSeats()))
                .filter(car -> filterDTO.getFuelType() == null || car.getFuelType().equals(filterDTO.getFuelType()))
                .filter(car -> filterDTO.getTransmissionType() == null ||
                        car.getTransmissionType().equals(filterDTO.getTransmissionType()))
                .filter(car -> filterDTO.getMinPrice() == null ||
                        car.getPricePerDay().compareTo(filterDTO.getMinPrice()) >= 0)
                .filter(car -> filterDTO.getMaxPrice() == null ||
                        car.getPricePerDay().compareTo(filterDTO.getMaxPrice()) <= 0)
                .collect(Collectors.toList());
    }

    private CarResponseDTO mapToCarResponseDTO(Car car, CarLocation location, List<CarImage> images) {
        CarResponseDTO dto = new CarResponseDTO();
        dto.setId(car.getId());
        dto.setBrand(car.getBrand());
        dto.setModel(car.getModel());
        dto.setNumberOfSeats(car.getNumberOfSeats());
        dto.setFuelType(car.getFuelType().toString());
        dto.setTransmissionType(car.getTransmissionType().toString());
        dto.setPricePerDay(car.getBasePricePerDay());
        dto.setLicensePlateNumber(car.getLicensePlateNumber());
        dto.setYearOfManufacture(car.getYearOfManufacture());
        dto.setDescription(car.getDescription());
        dto.setFuelConsumption(car.getFuelConsumption());

        // Set owner name
        userRepository.findById(car.getOwner().getId())
                .ifPresent(owner -> dto.setOwnerName(owner.getUsername()));

        // Set location
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setProvince(location.getProvince());
        locationDTO.setDistrict(location.getDistrict());
        locationDTO.setWard(location.getWard());
        locationDTO.setAddressDetails(location.getAddressDetails());
        dto.setLocation(locationDTO);

        // Set images
        List<CarImageDTO> imageUrls = images.stream()
                .map(image -> CarImageDTO.builder()
                        .order(image.getImageOrder())
                        .imageUrl(image.getImageUrl())
                        .build()
                )
                .collect(Collectors.toList());
        dto.setImageUrls(imageUrls);

        return dto;
    }
}