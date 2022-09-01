package ru.practicum.shareit.booking;

public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus()
        );
    }

    public static Booking toBooking(BookingDto bookingDto) {
        Booking booking = new Booking(
                bookingDto.getStart(),
                bookingDto.getEnd(),
                bookingDto.getItem()
        );
        return booking;
    }


}
