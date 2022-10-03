package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.Update;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient client;

    @PostMapping
    public ResponseEntity<Object> create(@Validated({Create.class}) @RequestBody UserRequestDto userDto) {
        log.info("Creating user {}", userDto);
        return client.create(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@Validated({Update.class}) @RequestBody UserRequestDto userDto,
                                         @PathVariable int userId) {
        log.info("Patching userID {} by user{}", userId, userDto);
        return client.update(userId, userDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> get(@PathVariable int userId) {
        log.info("Get userId {}", userId);
        return client.get(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("Get all users");
        return client.getAll();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable int userId) {
        log.info("Delete userId {}", userId);
        return client.delete(userId);
    }
}