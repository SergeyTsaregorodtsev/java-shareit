package ru.practicum.shareit.booking;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BookingDtoShort {
    int id;
    int bookerId;
    LocalDateTime start;
    LocalDateTime end;
    Booking.Status status;
}
