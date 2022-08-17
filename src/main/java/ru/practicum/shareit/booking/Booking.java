package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class Booking {
    private final int bookingID;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final int itemID;
    private final int bookerID;


    private enum Status {
        WAITING,    // Новое бронирование, ожидает одобрения
        APPROVED,   // Бронирование подтверждено владельцем
        REJECTED,   // Бронировние отклонено владельцем
        CANCELED    // Бронирование отменено создателем
    }
}
