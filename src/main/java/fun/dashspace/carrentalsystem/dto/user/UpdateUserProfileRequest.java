package fun.dashspace.carrentalsystem.dto.user;

import fun.dashspace.carrentalsystem.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateUserProfileRequest {
    private String username;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private Gender gender;
}
