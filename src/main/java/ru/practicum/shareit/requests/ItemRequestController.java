package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService service;

    @PostMapping
    public ItemRequestDto addRequest(@RequestHeader("X-Sharer-User-Id") int userId,
                                     @RequestBody ItemRequestDto dto) {
        log.trace("Получен POST-запрос на запрос от пользователя ID {}.", userId);
        return service.addRequest(dto, userId);
    }

    @GetMapping
    public List<ItemRequestDto> getOwnRequests(@RequestHeader("X-Sharer-User-Id") int userId) {
        log.trace("Получен GET-запрос на свои запросы от пользователя ID {}.", userId);
        return service.getOwnRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") int userId,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        log.trace("Получен GET-запрос на запросы from = {}, size = {}.", from, size);
        return service.getAllRequests(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto get(@RequestHeader("X-Sharer-User-Id") int userId,
                              @PathVariable int requestId) {
        log.trace("Получен GET-запрос на запрос ID {}.", requestId);
        return service.get(requestId, userId);
    }
}