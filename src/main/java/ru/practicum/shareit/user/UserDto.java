package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UserDto {
    private final int id;
    @NotBlank(groups = {Create.class})
    private final String name;
    @Email(groups = {Create.class, Update.class})
    @NotBlank(groups = {Create.class})
    private final String email;
}
