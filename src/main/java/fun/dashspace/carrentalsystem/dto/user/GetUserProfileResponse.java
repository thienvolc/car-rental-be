package fun.dashspace.carrentalsystem.dto.user;

import fun.dashspace.carrentalsystem.enums.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class GetUserProfileResponse {
    private String username;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String avatarUrl;
}
