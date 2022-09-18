package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingMapperTest {
    static LocalDateTime start = LocalDateTime.now().plusHours(1);
    static LocalDateTime end = LocalDateTime.now().plusDays(1);
    static User user = new User(1,"John Doe", "john.doe@mail.com");
    static User booker = new User(2, "Richard Roe", "RichardRoe@mail.com");
    static Item item = new Item(1, "Hammer", "John Doe's hammer", user,true,0);

    @Test
    void toBookingDto() {
        Booking booking = new Booking(1, item, booker, start,end, Booking.Status.APPROVED);
        Object object = BookingMapper.toBookingDto(booking);
        assertEquals(BookingDtoOut.class, object.getClass());
        BookingDtoOut bookingDto = (BookingDtoOut) object;
        assertEquals(ItemMapper.toItemDto(item), bookingDto.getItem());
        assertEquals(UserMapper.toUserDto(booker), bookingDto.getBooker());
        assertEquals(start, bookingDto.getStart());
        assertEquals(end, bookingDto.getEnd());
        assertEquals(Booking.Status.APPROVED, bookingDto.getStatus());
    }

    @Test
    void toBooking() {
        BookingDto bookingDto = new BookingDto(1,1,start,end, Booking.Status.APPROVED);
        Object object = BookingMapper.toBooking(bookingDto, item, booker, Booking.Status.APPROVED);
        assertEquals(Booking.class, object.getClass());
        Booking booking = (Booking) object;
        assertEquals(item, booking.getItem());
        assertEquals(booker, booking.getBooker());
        assertEquals(start, booking.getStart());
        assertEquals(end, booking.getEnd());
        assertEquals(Booking.Status.APPROVED, booking.getStatus());
    }
}