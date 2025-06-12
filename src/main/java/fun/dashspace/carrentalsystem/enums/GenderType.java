package fun.dashspace.carrentalsystem.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GenderType {
    male("male"),
    female("female"),
    other("other");

    private final String gender;
}
