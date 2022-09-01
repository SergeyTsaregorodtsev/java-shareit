package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.user.*;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingMapper mapper;

    @Override
    public BookingDtoOut addBooking(BookingDto bookingDto, int userId) {
        if (bookingDto.getStart().isBefore(LocalDateTime.now())
                || bookingDto.getEnd().isBefore(LocalDateTime.now())
                || bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new BadParameterException("Неверно указано время бронирования.");
        }
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("Указанный пользователь не существует.");
        }
        int itemId = bookingDto.getItemId();
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new EntityNotFoundException("Неверно указан ID вещи.");
        } else if (!item.get().isAvailable()) {
            throw new BadParameterException("Указанная вещь недоступна для бронирования.");
        }

        Booking booking = mapper.toBooking(bookingDto);
        booking.setBooker(userId);
        booking.setStatus(Booking.Status.WAITING);
        bookingRepository.save(booking);
        log.trace("Добавлено бронирование ID {}, вещь ID {}.", booking.getItem(), userId);
        return mapper.toBookingDto(booking);
    }

    @Override
    public BookingDtoOut approve(int bookingId, int userId, String approve) {
        try {
            ApproveValues.valueOf(approve.toUpperCase());
        } catch (IllegalArgumentException ignored) {
            throw new ValidationException("Неверный статус бронирования.");
        }

        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new EntityNotFoundException("Неверно указан ID бронирования.");
        }
        Booking approvedBooking = booking.get();

        int itemId = approvedBooking.getItem();
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isPresent() && item.get().getOwnerId() != userId) {
            throw new ValidationException("Подтердить бронирование может только владелец вещи.");
        }

        Booking.Status status;
        if (approve.equalsIgnoreCase(ApproveValues.TRUE.name())) {
            status = Booking.Status.APPROVED;
        } else {
            status = Booking.Status.REJECTED;
        }
        approvedBooking.setStatus(status);
        bookingRepository.save(approvedBooking);
        log.trace("Статус бронирования ID {} обновлён до {}.", bookingId, status);
        return mapper.toBookingDto(approvedBooking);
    }

    @Override
    public BookingDtoOut get(int bookingId, int userId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new EntityNotFoundException("Неверно указан ID бронирования.");
        }
        Booking currentBooking = booking.get();
        int bookerId = currentBooking.getBooker();
        Optional<Item> item = itemRepository.findById(currentBooking.getItem());
        if (item.isEmpty()) {
            throw new EntityNotFoundException("Неверно указан ID бронирования.");
        }
        int ownerId = item.get().getOwnerId();
        if (userId != bookerId & userId != ownerId) {
            throw new ValidationException("Пользователь не может запрашивать данные.");
        }
        return mapper.toBookingDto(bookingRepository.findBookingById(bookingId));
    }

    @Override
    public List<BookingDtoOut> getAll(int userId, String state) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("Указанный пользователь не существует.");
        }
        try {
            StateValues.valueOf(state);
        } catch (IllegalArgumentException ignored) {
            throw new ValidationException("Неверный статус бронирования.");
        }
        List<Booking> bookings = new ArrayList<>();
        List<BookingDtoOut> bookingDtos = new ArrayList<>();
        switch (state) {
            case "ALL": {
                bookings = bookingRepository.findBookingsByBookerOrderByStartDesc(userId);
                break;
            }
            case "CURRENT": {
                break;
            }
            case "PAST": {
                bookings = bookingRepository.findBookingsByBookerAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now());
                break;
            }
            case "FUTURE": {
                bookings = bookingRepository.findBookingsByBookerAndStartIsAfterOrderByStartDesc(userId, LocalDateTime.now());
                break;
            }
            default: {
            }
        }
        log.trace("Получено записей бронирования {}.", bookings.size());
        for (Booking booking : bookings) {
            bookingDtos.add(mapper.toBookingDto(booking));
        }
        return bookingDtos;
    }

    private enum ApproveValues {
        TRUE,
        FALSE
    }

    private enum StateValues {
        ALL,
        CURRENT,
        PAST,
        FUTURE,
        WAITING,
        REJECTED
    }
}
