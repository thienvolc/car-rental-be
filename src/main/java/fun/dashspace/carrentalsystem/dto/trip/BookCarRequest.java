package fun.dashspace.carrentalsystem.dto.trip;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class BookCarRequest {
    private Integer carId;

    private LocalDate pickupDate;
    private LocalDate returnDate;
    private LocalTime pickupTime;
    private LocalTime returnTime;
}
