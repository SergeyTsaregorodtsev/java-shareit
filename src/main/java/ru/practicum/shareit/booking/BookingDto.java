package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDto {
    private final int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private int item;
    private int booker;
    private Booking.Status status;

}
