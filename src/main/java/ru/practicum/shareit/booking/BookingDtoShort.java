package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDtoShort {
    private final int id;
    private int bookerId;
    private LocalDateTime start;
    private LocalDateTime end;
    private Booking.Status status;
}
