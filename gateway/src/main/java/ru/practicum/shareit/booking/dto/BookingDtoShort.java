package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

public class BookingDtoShort {
    int id;
    int bookerId;
    LocalDateTime start;
    LocalDateTime end;
    BookingState status;
}
