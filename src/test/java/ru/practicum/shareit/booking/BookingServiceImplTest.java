package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class BookingServiceImplTest {
    BookingRepository bookingRepository;
    ItemRepository itemRepository;
    UserRepository userRepository;

    BookingService bookingService;
    LocalDateTime start = LocalDateTime.now().plusHours(1);
    LocalDateTime end = LocalDateTime.now().plusDays(1);

    @BeforeEach
    void setUp() {
        bookingRepository = mock(BookingRepository.class);
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);
        bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
    }

    @Test
    void addBooking() {
        User booker = new User(1, "John Doe", "JohnDoe@mail.com");
        User owner = new User(2, "Richard Roe", "RichardRoe@mail.com");
        Item item = new Item(1, "Hammer", "Richard Roe's hammer", owner,true,0);
        BookingDto bookingDto = new BookingDto(1,1, start, end, Booking.Status.WAITING);
        Booking booking = BookingMapper.toBooking(bookingDto, item, booker, Booking.Status.WAITING);
        when(bookingRepository.save(any())).thenReturn(booking);
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));

        BookingDtoOut bookingDtoOut = bookingService.addBooking(bookingDto,1);
        verify(bookingRepository, times(1)).save(booking);
        assertEquals("Hammer", bookingDtoOut.getItem().getName());
    }
}