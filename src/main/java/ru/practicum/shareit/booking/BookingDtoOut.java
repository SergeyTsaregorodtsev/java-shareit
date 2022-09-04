package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.user.UserDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDtoOut {
    private final int id;
    private ItemDto item;
    private UserDto booker;
    private LocalDateTime start;
    private LocalDateTime end;
    private Booking.Status status;
}
