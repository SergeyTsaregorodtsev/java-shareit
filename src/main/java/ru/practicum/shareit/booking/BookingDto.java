package ru.practicum.shareit.booking;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {
    int itemId;
    int bookerId;
    LocalDateTime start;
    LocalDateTime end;
    Booking.Status status;
}
