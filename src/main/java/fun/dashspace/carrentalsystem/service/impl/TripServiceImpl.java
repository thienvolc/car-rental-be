package fun.dashspace.carrentalsystem.service.impl;

import fun.dashspace.carrentalsystem.dto.trip.BookCarRequest;
import fun.dashspace.carrentalsystem.entity.Car;
import fun.dashspace.carrentalsystem.entity.Trip;
import fun.dashspace.carrentalsystem.entity.TripCancellation;
import fun.dashspace.carrentalsystem.enums.TripStatus;
import fun.dashspace.carrentalsystem.exception.custom.resource.ResourceNotFoundException;
import fun.dashspace.carrentalsystem.exception.custom.validation.BadRequestException;
import fun.dashspace.carrentalsystem.repository.CarRepo;
import fun.dashspace.carrentalsystem.repository.TripCancellationRepo;
import fun.dashspace.carrentalsystem.repository.TripRepo;
import fun.dashspace.carrentalsystem.security.AuthenticateFacade;
import fun.dashspace.carrentalsystem.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {

    private final CarRepo carRepo;
    private final TripRepo tripRepo;
    private final AuthenticateFacade authenticateFacade;
    private final TripCancellationRepo tripCancellationRepo;

    @Override
    public void create(BookCarRequest req) {
        var car = carRepo.findById(req.getCarId())
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + req.getCarId()));

        validateCarAvailability(car, req);

        tripRepo.save(buildTrip(car, req));
    }

    private void validateCarAvailability(Car car, BookCarRequest req) {
        Instant startTime = toInstant(req.getPickupDate(), req.getPickupTime());
        Instant endTime = toInstant(req.getReturnDate(), req.getReturnTime());
        if (isConflictingBooking(car, startTime, endTime))
            throw new BadRequestException("Booking car already exists");
    }

    public Instant toInstant(LocalDate date, LocalTime time) {
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        ZoneId zone = ZoneId.of("Asia/Ho_Chi_Minh");
        return dateTime.atZone(zone).toInstant();
    }

    private boolean isConflictingBooking(Car car, Instant startTime, Instant endTime) {
        return tripRepo.existsByCarAndStatusInAndPickupDateLessThanEqualAndReturnDateGreaterThanEqual(
                car,
                List.of(TripStatus.PENDING, TripStatus.IN_PROGRESS, TripStatus.APPROVED),
                endTime,
                startTime
        );
    }


    private Trip buildTrip(Car car, BookCarRequest req) {
        Instant startTime = toInstant(req.getPickupDate(), req.getPickupTime());
        Instant endTime = toInstant(req.getReturnDate(), req.getReturnTime());
        long rentalDays = Duration.between(startTime, endTime).toDays();
        if (rentalDays < 1)
            rentalDays = 1;

        BigDecimal totalAmount = car.getBasePricePerDay().multiply(BigDecimal.valueOf(rentalDays));

        return Trip.builder()
                .car(car)
                .renter(authenticateFacade.getCurrentUser())
                .totalAmount(totalAmount)
                .pickupDate(startTime)
                .returnDate(endTime)
                .build();
    }

    @Override
    public void cancel(Integer tripId) {
        var trip = tripRepo.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + tripId));

        if (trip.getStatus() != TripStatus.PENDING)
            throw new BadRequestException("Only pending trips can be cancelled");

        tripCancellationRepo.save(buildTripCancellation(trip));

        trip.setStatus(TripStatus.CANCELLED);
        tripRepo.save(trip);
    }

    private TripCancellation buildTripCancellation(Trip trip) {
        return TripCancellation.builder()
                .trip(trip)
                .cancelledAt(Instant.now())
                .cancelledByUser(authenticateFacade.getCurrentUser())
                .build();
    }
}
