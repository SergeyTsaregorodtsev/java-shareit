package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService service;

    @PostMapping
    public BookingDtoOut add(@RequestHeader("X-Sharer-User-Id") int userId,
                       @Validated({Create.class}) @RequestBody BookingDto bookingDto) {
        log.trace("Получен POST-запрос на бронирование вещи ID {} от пользователя ID {}.", bookingDto.getItemId(), userId);
        return service.addBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOut approve(@RequestHeader("X-Sharer-User-Id") int userId,
                           @PathVariable int bookingId,
                           @RequestParam(value = "approved") String approved) {
        log.trace("Получен PATCH-запрос подтверждения брони ID {} от пользователя ID {}.", bookingId, userId);
        return service.approve(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOut get(@RequestHeader("X-Sharer-User-Id") int userId,
                        @PathVariable int bookingId) {
        log.trace("Получен GET-запрос брони ID {} от пользователя ID {}.", bookingId, userId);
        return service.get(bookingId, userId);
    }

    @GetMapping()
    public List<BookingDtoOut> getAll(@RequestHeader("X-Sharer-User-Id") int userId,
                                      @RequestParam(value = "state", defaultValue = "ALL") String state,
                                      @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                      @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        log.trace("Получен GET-запрос брони от пользователя ID {}, статус - {}.", userId, state);
        return service.getAll(userId, state, false, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDtoOut> getOwn(@RequestHeader("X-Sharer-User-Id") int userId,
                                      @RequestParam(value = "state", defaultValue = "ALL") String state,
                                      @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                      @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        log.trace("Получен GET-запрос бронирований на вещи пользователя ID {}, статус - {}.", userId, state);
        return service.getAll(userId, state, true, from, size);
    }


}
