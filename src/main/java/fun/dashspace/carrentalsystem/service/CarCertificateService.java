package fun.dashspace.carrentalsystem.service;

import fun.dashspace.carrentalsystem.dto.car.UpdateCarCertificateRequest;

public interface CarCertificateService {
    void uploadCarCertificate(Integer carId, UpdateCarCertificateRequest req);
}
