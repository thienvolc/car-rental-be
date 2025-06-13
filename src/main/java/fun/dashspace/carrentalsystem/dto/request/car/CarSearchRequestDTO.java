package fun.dashspace.carrentalsystem.dto.request.car;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CarSearchRequestDTO {
    private String location;
    private LocalDate pickupDate;
    private LocalDate returnDate;
    private LocalTime pickupTime;
    private LocalTime returnTime;
}