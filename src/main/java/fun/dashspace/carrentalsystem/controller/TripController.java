package fun.dashspace.carrentalsystem.controller;

import fun.dashspace.carrentalsystem.dto.common.response.ApiResponse;
import fun.dashspace.carrentalsystem.dto.trip.BookCarRequest;
import fun.dashspace.carrentalsystem.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> create(@RequestBody BookCarRequest req) {
        tripService.create(req);
        return ResponseEntity.ok(ApiResponse.ok("Create trip successful"));
    }

    @PostMapping("/{tripId}/cancel")
    public ResponseEntity<ApiResponse<String>> cancel(@PathVariable Integer tripId) {
        tripService.cancel(tripId);
        return ResponseEntity.ok(ApiResponse.ok("Cancle trip successful"));
    }
}
