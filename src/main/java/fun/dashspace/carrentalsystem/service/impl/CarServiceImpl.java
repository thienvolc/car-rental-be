package fun.dashspace.carrentalsystem.service.impl;

import fun.dashspace.carrentalsystem.dto.car.*;
import fun.dashspace.carrentalsystem.entity.Car;
import fun.dashspace.carrentalsystem.entity.CarCertificate;
import fun.dashspace.carrentalsystem.entity.CarImage;
import fun.dashspace.carrentalsystem.entity.CarLocation;
import fun.dashspace.carrentalsystem.exception.custom.resource.ResourceNotFoundException;
import fun.dashspace.carrentalsystem.exception.custom.validation.ResourceAlreadyExistsException;
import fun.dashspace.carrentalsystem.repository.CarRepo;
import fun.dashspace.carrentalsystem.security.AuthenticateFacade;
import fun.dashspace.carrentalsystem.service.CarImageService;
import fun.dashspace.carrentalsystem.service.CarLocationService;
import fun.dashspace.carrentalsystem.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final AuthenticateFacade authenticateFacade;
    private final CarRepo carRepo;
    private final CarLocationService carLocationService;
    private final CarImageService carImageService;

    @Override
    public void createCar(PostCarRequest req) {
        validateLicensePlateNumberNotExists(req.getLicensePlateNumber());
        CarLocation location = carLocationService.createCarLocation(req.getLocation());
        carRepo.save(buildCar(req, location));
    }

    private void validateLicensePlateNumberNotExists(String licensePlateNumber) {
        if (carRepo.existsByLicensePlateNumber(licensePlateNumber))
            throw new ResourceAlreadyExistsException("Car with license plate number " + licensePlateNumber + " already exists.");
    }

    private Car buildCar(PostCarRequest req, CarLocation location) {
        return Car.builder()
                .owner(authenticateFacade.getCurrentUser())
                .location(location)
                .brand(req.getBrand())
                .model(req.getModel())
                .yearOfManufacture(req.getYearOfManufacture())
                .basePricePerDay(req.getBasePricePerDay())
                .description(req.getDescription())
                .licensePlateNumber(req.getLicensePlateNumber())
                .fuelConsumption(req.getFuelConsumption())
                .fuelType(req.getFuelType())
                .numberOfSeats(req.getNumberOfSeats())
                .transmissionType(req.getTransmissionType())
                .build();
    }

    @Override
    public void updateCarRentalInfo(Integer carId, UpdateCarRentalInfoRequest req) {
        var car = getCarOrThrow(carId);
        car.setBasePricePerDay(req.getBasePricePerDay());
        car.setDescription(req.getDescription());
        carRepo.save(car);
    }

    @Override
    public void validateCarOwnerShip(Integer carId) {
        if (!carRepo.existsByIdAndOwner(carId, authenticateFacade.getCurrentUser()))
            throw new ResourceNotFoundException("Car not found or you do not have permission to access this car.");
    }

    @Override
    @Transactional(readOnly = true)
    public GetCarPortalResponse getCarPortalDetails(Integer carId) {
        var car = getCarWithImages(carId);
        return toGetCarResponse(car);
    }

    public Car getCarWithImages(Integer carId) {
        return carRepo.findByIdWithImages(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + carId));
    }

    private GetCarPortalResponse toGetCarResponse(Car car) {
        var res = GetCarPortalResponse.builder()
                .carId(car.getId())
                .ownerId(car.getOwner().getId())
                .brand(car.getBrand())
                .model(car.getModel())
                .yearOfManufacture(car.getYearOfManufacture())
                .basePricePerDay(car.getBasePricePerDay())
                .description(car.getDescription())
                .licensePlateNumber(car.getLicensePlateNumber())
                .fuelConsumption(car.getFuelConsumption())
                .fuelType(car.getFuelType())
                .numberOfSeats(car.getNumberOfSeats())
                .status(car.getStatus())
                .approvalStatus(car.getApprovalStatus())
                .transmissionType(car.getTransmissionType())
                .location(toCarLocationDto(car.getLocation()))
                .images(toCarImageDtoList(car.getImages()))
                .build();

        if (car.getCertificate() != null)
            res.setCertificate(toCertificateDto(car.getCertificate()));

        return res;
    }

    private CarLocationDto toCarLocationDto(CarLocation location) {
        return CarLocationDto.builder()
                .province(location.getProvince())
                .district(location.getDistrict())
                .ward(location.getWard())
                .addressDetails(location.getAddressDetails())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
    }

    private List<CarImageDto> toCarImageDtoList(Set<CarImage> images) {
        return images.stream()
                .map(this::toCarImageDto)
                .toList();
    }

    private CarImageDto toCarImageDto(CarImage image) {
        return CarImageDto.builder()
                .imageUrl(image.getImageUrl())
                .imageOrder(image.getImageOrder())
                .build();
    }

    private CarCertificateDto toCertificateDto(CarCertificate cert) {
        return CarCertificateDto.builder()
                .inspectionImageUrl(cert.getInspectionUrl())
                .insuranceImageUrl(cert.getInsuranceUrl())
                .registrationImageUrl(cert.getRegistrationUrl())
                .backImageUrl(cert.getBackImageUrl())
                .frontImageUrl(cert.getFrontImageUrl())
                .leftImageUrl(cert.getLeftImageUrl())
                .rightImageUrl(cert.getRightImageUrl())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public GetAllCarsResponse getAllOwnedCars() {
        var user = authenticateFacade.getCurrentUser();
        var cars = carRepo.findAllByOwner(user);
        List<CarDisplayItem> carItemList = buildCarItemList(cars);
        return new GetAllCarsResponse(carItemList);
    }

    @Override
    @Transactional(readOnly = true)
    public GetAllCarsResponse getAllCars() {
        var cars = carRepo.findAll();
        List<CarDisplayItem> carItemList = buildCarItemList(cars);
        return new GetAllCarsResponse(carItemList);
    }

    private List<CarDisplayItem> buildCarItemList(List<Car> cars) {
        return cars.stream()
                .map(this::toCarDisplayItem)
                .toList();
    }

    private CarDisplayItem toCarDisplayItem(Car car) {
        return CarDisplayItem.builder()
                .carId(car.getId())
                .mainImageUrl(carImageService.getMainImageUrlByCarId(car.getId()))
                .basePricePerDay(car.getBasePricePerDay())
                .yearOfManufacture(car.getYearOfManufacture())
                .brand(car.getBrand())
                .model(car.getModel())
                .location(toCarLocationDto(car.getLocation()))
                .status(car.getStatus())
                .approvalStatus(car.getApprovalStatus())
                .build();
    }

    @Override
    public void updateCarApprovalStatus(Integer carId, ReviewCarRequest req) {
        var car = getCarOrThrow(carId);
        car.setApprovalStatus(req.getStatus());
        carRepo.save(car);
    }

    private Car getCarOrThrow(Integer carId) {
        return carRepo.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + carId));
    }

    @Override
    public void updateCarStatus(Integer carId, UpdateCarStatusRequest req) {
        var car = getCarOrThrow(carId);
        car.setStatus(req.getStatus());
        carRepo.save(car);
    }

    @Override
    public CarResponseDTO getCarDetails(Integer carId) {
        var car = getCarWithImages(carId);
        return toCarResponseDTO(car);
    }

    private CarResponseDTO toCarResponseDTO(Car car) {
        return CarResponseDTO.builder()
                .id(car.getId())
                .brand(car.getBrand())
                .model(car.getModel())
                .yearOfManufacture(car.getYearOfManufacture())
                .ownerName(car.getOwner().getUsername())
                .pricePerDay(car.getBasePricePerDay())
                .brand(car.getBrand())
                .location(toCarLocationDto(car.getLocation()))
                .imageUrls(toCarImageDtoList(car.getImages()))
                .description(car.getDescription())
                .licensePlateNumber(car.getLicensePlateNumber())
                .fuelConsumption(car.getFuelConsumption())
                .fuelType(car.getFuelType())
                .numberOfSeats(car.getNumberOfSeats())
                .transmissionType(car.getTransmissionType())
                .build();
    }
}
