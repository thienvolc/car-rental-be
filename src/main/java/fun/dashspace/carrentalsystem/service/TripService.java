package fun.dashspace.carrentalsystem.service;

import fun.dashspace.carrentalsystem.dto.trip.BookCarRequest;

public interface TripService {
    void create(BookCarRequest req);

    void cancel(Integer tripId);
}
