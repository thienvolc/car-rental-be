package fun.dashspace.carrentalsystem.service.impl;

import fun.dashspace.carrentalsystem.dto.car.UpdateCarCertificateRequest;
import fun.dashspace.carrentalsystem.entity.Car;
import fun.dashspace.carrentalsystem.entity.CarCertificate;
import fun.dashspace.carrentalsystem.exception.custom.resource.ResourceNotFoundException;
import fun.dashspace.carrentalsystem.repository.CarCertificateRepo;
import fun.dashspace.carrentalsystem.repository.CarRepo;
import fun.dashspace.carrentalsystem.service.CarCertificateService;
import fun.dashspace.carrentalsystem.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarCertificateServiceImpl implements CarCertificateService {

    private final CarCertificateRepo carCertificateRepo;
    private final CarRepo carRepo;
    private final ImageUploadService imageUploadService;

    @Override
    public void uploadCarCertificate(Integer carId, UpdateCarCertificateRequest req) {
        var car = getCarOrThrow(carId);
        var existingCertificate = Optional.ofNullable(car.getCertificate());

        if (existingCertificate.isPresent())
            updateExistingCarCertificate(existingCertificate.get(), req);
        else
            createNewCarCertificate(car, req);
    }

    private void createNewCarCertificate(Car car, UpdateCarCertificateRequest req) {
        var cert = new CarCertificate();
        cert.setCar(car);

        if (req.getRegistrationImage() != null) {
            var url = imageUploadService.uploadFile(req.getRegistrationImage()).getSecureUrl();
            cert.setRegistrationUrl(url);
        }

        if (req.getInsuranceImage() != null) {
            var url = imageUploadService.uploadFile(req.getInsuranceImage()).getSecureUrl();
            cert.setInsuranceUrl(url);
        }

        if (req.getInspectionImage() != null) {
            var url = imageUploadService.uploadFile(req.getInspectionImage()).getSecureUrl();
            cert.setInspectionUrl(url);
        }

        if (req.getBackImage() != null) {
            var url = imageUploadService.uploadFile(req.getBackImage()).getSecureUrl();
            cert.setBackImageUrl(url);
        }

        if (req.getFrontImage() != null) {
            var url = imageUploadService.uploadFile(req.getFrontImage()).getSecureUrl();
            cert.setFrontImageUrl(url);
        }

        if (req.getLeftImage() != null) {
            var url = imageUploadService.uploadFile(req.getLeftImage()).getSecureUrl();
            cert.setLeftImageUrl(url);
        }

        if (req.getRightImage() != null) {
            var url = imageUploadService.uploadFile(req.getRightImage()).getSecureUrl();
            cert.setRightImageUrl(url);
        }

        carCertificateRepo.save(cert);
    }

    private void updateExistingCarCertificate(CarCertificate cert, UpdateCarCertificateRequest req) {
        if (req.getRegistrationImage() != null) {
            imageUploadService.deleteFile(cert.getRegistrationUrl());
            var url = imageUploadService.uploadFile(req.getRegistrationImage()).getSecureUrl();
            cert.setRegistrationUrl(url);
        }

        if (req.getInsuranceImage() != null) {
            imageUploadService.deleteFile(cert.getInsuranceUrl());
            var url = imageUploadService.uploadFile(req.getInsuranceImage()).getSecureUrl();
            cert.setInsuranceUrl(url);
        }

        if (req.getInspectionImage() != null) {
            imageUploadService.deleteFile(cert.getInspectionUrl());
            var url = imageUploadService.uploadFile(req.getInspectionImage()).getSecureUrl();
            cert.setInspectionUrl(url);
        }

        if (req.getBackImage() != null) {
            imageUploadService.deleteFile(cert.getBackImageUrl());
            var url = imageUploadService.uploadFile(req.getBackImage()).getSecureUrl();
            cert.setBackImageUrl(url);
        }

        if (req.getFrontImage() != null) {
            imageUploadService.deleteFile(cert.getFrontImageUrl());
            var url = imageUploadService.uploadFile(req.getFrontImage()).getSecureUrl();
            cert.setFrontImageUrl(url);
        }

        if (req.getLeftImage() != null) {
            imageUploadService.deleteFile(cert.getLeftImageUrl());
            var url = imageUploadService.uploadFile(req.getLeftImage()).getSecureUrl();
            cert.setLeftImageUrl(url);
        }

        if (req.getRightImage() != null) {
            imageUploadService.deleteFile(cert.getRightImageUrl());
            var url = imageUploadService.uploadFile(req.getRightImage()).getSecureUrl();
            cert.setRightImageUrl(url);
        }

        carCertificateRepo.save(cert);
    }

    private Car getCarOrThrow(Integer carId) {
        return carRepo.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + carId));
    }
}
