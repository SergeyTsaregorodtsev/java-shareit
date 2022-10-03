package ru.practicum.shareit.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDto {
    int id;
    String name;
    String email;
}