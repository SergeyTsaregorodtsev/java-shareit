package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingService {

    BookingDtoOut addBooking(BookingDto bookingDto, int userId);

    BookingDtoOut approve(int bookingId, int userId, String approved);

    BookingDtoOut get(int bookingId, int userId);

    List<BookingDtoOut> getAll(int userId, String state, boolean isOwn, int from, int size);
}
