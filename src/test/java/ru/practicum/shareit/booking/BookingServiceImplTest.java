package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import ru.practicum.shareit.exceptions.BadParameterException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
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

    User booker = new User(1, "John Doe", "JohnDoe@mail.com");
    User owner = new User(2, "Richard Roe", "RichardRoe@mail.com");
    Item item = new Item(1, "Hammer", "Richard Roe's hammer", owner,true,0);
    BookingDto bookingDto;
    Booking booking;

    @BeforeEach
    void setUp() {
        bookingRepository = mock(BookingRepository.class);
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);
        bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        bookingDto = new BookingDto(1, 1, start, end, Booking.Status.WAITING);
    }

    @Test
    void addBooking() {
        booking = BookingMapper.toBooking(bookingDto, item, booker, Booking.Status.WAITING);
        when(bookingRepository.save(any())).thenReturn(booking);
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));

        BookingDtoOut bookingDtoOut = bookingService.addBooking(bookingDto,1);
        verify(bookingRepository, times(1)).save(booking);
        assertEquals("Hammer", bookingDtoOut.getItem().getName());
    }

    @Test
    void approveFailWithInvalidApproveStatus() {
        Exception e = assertThrows(ValidationException.class,
                () -> bookingService.approve(1, 2, "TRUE_"));
        assertEquals("Неверный статус бронирования.", e.getMessage(),
                "Неверная обработка ошибки со статусом подтверждения.");
    }

    @Test
    void approveFailWithInvalidBookingId() {
        when(bookingRepository.findById(any())).thenReturn(Optional.empty());
        Exception e = assertThrows(EntityNotFoundException.class,
                () -> bookingService.approve(1, 2, "TRUE"));
        assertEquals("Неверно указан ID бронирования.", e.getMessage(),
                "Неверная обработка ошибки с ID бронирования.");
    }

    @Test
    void approveFailWithAlreadyChangedState() {
        booking = BookingMapper.toBooking(bookingDto, item, booker, Booking.Status.APPROVED);
        when(bookingRepository.findById(any())).thenReturn(Optional.of(booking));
        Exception e = assertThrows(BadParameterException.class,
                () -> bookingService.approve(1, 2, "TRUE"));
        assertEquals("Статус бронирования уже изменён.", e.getMessage(),
                "Неверная обработка ошибки с уже подтверждённым бронированием.");
    }

    @Test
    void approveFailWithAlienApprove() {
        booking = BookingMapper.toBooking(bookingDto, item, booker, Booking.Status.WAITING);
        when(bookingRepository.findById(any())).thenReturn(Optional.of(booking));
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        Exception e = assertThrows(EntityNotFoundException.class,
                () -> bookingService.approve(1,100, "TRUE"));
        assertEquals("Подтердить бронирование может только владелец вещи.", e.getMessage(),
                "Неверная обработка ошибки с подтверждением брони не владельцем вещи.");
    }

    @Test
    void approveTrue() {
        booking = BookingMapper.toBooking(bookingDto, item, booker, Booking.Status.WAITING);
        when(bookingRepository.findById(any())).thenReturn(Optional.of(booking));
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        BookingDtoOut bookingDtoOut = bookingService.approve(1,2, "TRUE");
        booking.setStatus(Booking.Status.APPROVED);

        verify(bookingRepository, times(1)).findById(1);
        verify(itemRepository, times(1)).findById(1);
        verify(bookingRepository, times(1)).save(booking);
        assertEquals(Booking.Status.APPROVED, bookingDtoOut.getStatus());
    }

    @Test
    void approveFalse() {
        booking = BookingMapper.toBooking(bookingDto, item, booker, Booking.Status.WAITING);
        when(bookingRepository.findById(any())).thenReturn(Optional.of(booking));
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        BookingDtoOut bookingDtoOut = bookingService.approve(1,2, "FALSE");
        booking.setStatus(Booking.Status.REJECTED);

        verify(bookingRepository, times(1)).findById(1);
        verify(itemRepository, times(1)).findById(1);
        verify(bookingRepository, times(1)).save(booking);
        assertEquals(Booking.Status.REJECTED, bookingDtoOut.getStatus());
    }

    @Test
    void get() {
        booking = BookingMapper.toBooking(bookingDto, item, booker, Booking.Status.WAITING);
        when(bookingRepository.findById(anyInt())).thenReturn(Optional.of(booking));
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));

        BookingDtoOut bookingDtoOut = bookingService.get(1,1);
        verify(bookingRepository, times(1)).findById(1);
        verify(itemRepository, times(1)).findById(1);
        assertEquals("Hammer", bookingDtoOut.getItem().getName());
    }

    @Test
    void getAll() {
        booking = BookingMapper.toBooking(bookingDto, item, booker, Booking.Status.WAITING);
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(booker));

        List<Booking> bookings = Collections.singletonList(booking);
        Pageable page = PageRequest.of(5 / 10, 10);
        Page<Booking> bookingPage = new PageImpl<>(bookings, page, bookings.size());

        when(bookingRepository.findBookingsByBookerIdOrderByStartDesc(anyInt(), any(PageRequest.class)))
                .thenReturn(bookingPage);
        List<BookingDtoOut> bookingsDto = bookingService.getAll(1, "ALL", false, 5, 10);
        verify(bookingRepository, times(1)).findBookingsByBookerIdOrderByStartDesc(1,page);
        assertEquals(1, bookingsDto.size());
        assertEquals("Hammer", bookingsDto.get(0).getItem().getName());

        when(bookingRepository.findOwn(anyInt(), any(PageRequest.class)))
                .thenReturn(bookingPage);
        bookingService.getAll(1, "ALL", true, 5, 10);
        verify(bookingRepository, times(1)).findOwn(1,page);

        when(bookingRepository.findOwnCurrent(anyInt(), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(bookingPage);
        bookingService.getAll(1, "CURRENT", true, 5, 10);
        verify(bookingRepository, times(1))
                .findOwnCurrent(anyInt(),any(LocalDateTime.class), any(PageRequest.class));

        when(bookingRepository.findBookingsByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                anyInt(), any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(bookingPage);
        bookingService.getAll(1, "CURRENT", false, 5, 10);
        verify(bookingRepository, times(1))
                .findBookingsByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                        anyInt(), any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class));

        when(bookingRepository.findOwnPast(anyInt(), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(bookingPage);
        bookingService.getAll(1, "PAST", true, 5, 10);
        verify(bookingRepository, times(1))
                .findOwnPast(anyInt(),any(LocalDateTime.class), any(PageRequest.class));

        when(bookingRepository.findBookingsByBookerIdAndEndIsBeforeOrderByStartDesc(
                anyInt(), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(bookingPage);
        bookingService.getAll(1, "PAST", false, 5, 10);
        verify(bookingRepository, times(1))
                .findBookingsByBookerIdAndEndIsBeforeOrderByStartDesc(
                        anyInt(), any(LocalDateTime.class), any(PageRequest.class));

        when(bookingRepository.findOwnFuture(anyInt(), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(bookingPage);
        bookingService.getAll(1, "FUTURE", true, 5, 10);
        verify(bookingRepository, times(1))
                .findOwnFuture(anyInt(),any(LocalDateTime.class), any(PageRequest.class));

        when(bookingRepository.findBookingsByBookerIdAndStartIsAfterOrderByStartDesc(
                anyInt(), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(bookingPage);
        bookingService.getAll(1, "FUTURE", false, 5, 10);
        verify(bookingRepository, times(1))
                .findBookingsByBookerIdAndStartIsAfterOrderByStartDesc(
                        anyInt(), any(LocalDateTime.class), any(PageRequest.class));

        when(bookingRepository.findOwnByStatus(anyInt(), any(Booking.Status.class), any(PageRequest.class)))
                .thenReturn(bookingPage);
        bookingService.getAll(1, "WAITING", true, 5, 10);
        verify(bookingRepository, times(1))
                .findOwnByStatus(1, Booking.Status.WAITING, page);

        when(bookingRepository.findBookingsByBookerIdAndStatus(anyInt(), any(Booking.Status.class), any(PageRequest.class)))
                .thenReturn(bookingPage);
        bookingService.getAll(1, "WAITING", false, 5, 10);
        verify(bookingRepository, times(1))
                .findBookingsByBookerIdAndStatus(1, Booking.Status.WAITING, page);
    }
}