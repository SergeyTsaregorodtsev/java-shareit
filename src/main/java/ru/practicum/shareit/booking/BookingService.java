package ru.practicum.shareit.booking;

public interface BookingService {

    BookingDto addBooking (BookingDto bookingDto, int userId);

    void approve (int bookingId, int userId, String approved);

    BookingDto get (int bookingId, int userId);
}
