package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    public UserDto create(@Validated({Create.class}) @RequestBody UserDto userDto) {
        log.trace("Получен POST-запрос на добавление пользователя {}.", userDto.getName());
        return service.addUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@Validated({Update.class}) @RequestBody UserDto userDto,
                          @PathVariable int userId) {
        log.trace("Получен PATCH-запрос на обновление пользователя {}, ID {}.", userDto.getName(), userId);
        return service.updateUser(userDto, userId);
    }

    @GetMapping("/{userId}")
    public UserDto get(@PathVariable int userId) {
        log.trace("Получен GET-запрос на пользователя ID {}.", userId);
        return service.getUser(userId);
    }

    @GetMapping
    public List<UserDto> get() {
        log.trace("Получен GET-запрос по всем пользователям.");
        return service.getUsers();
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable int userId) {
        log.trace("Получен DELETE-запрос на удаление пользователя ID {}.", userId);
        service.removeUser(userId);
    }
}
