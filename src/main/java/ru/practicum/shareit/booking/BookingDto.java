package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDto {
    private int itemId;
    private int bookerId;
    private LocalDateTime start;
    private LocalDateTime end;
    private Booking.Status status;
}
