package ru.practicum.shareit.user;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDto {
    int id;
    @NotBlank(groups = {Create.class})
    String name;
    @Email(groups = {Create.class, Update.class})
    @NotBlank(groups = {Create.class})
    String email;
}