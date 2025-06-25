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
    public void validateCarOwnerShip(Integer carId) {
        if (!carRepo.existsByIdAndOwner(carId, authenticateFacade.getCurrentUser()))
            throw new ResourceNotFoundException("Car not found or you do not have permission to access this car.");
    }

    @Override
    @Transactional(readOnly = true)
    public GetCarResponse getCarDetails(Integer carId) {
        var car = getCarWithImages(carId);
        return toGetCarResponse(car);
    }

    public Car getCarWithImages(Integer carId) {
        return carRepo.findByIdWithImages(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + carId));
    }

    private GetCarResponse toGetCarResponse(Car car) {
        var res = GetCarResponse.builder()
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
}
