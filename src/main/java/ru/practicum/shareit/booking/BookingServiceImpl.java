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
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

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
        }
        if (!item.get().isAvailable()) {
            throw new BadParameterException("Указанная вещь недоступна для бронирования.");
        }
        if (item.get().getOwner().getId() == userId) {
            throw new EntityNotFoundException("Владелец вещи не может её забронировать.");
        }

        Booking booking = BookingMapper.toBooking(bookingDto, item.get(), user.get(), Booking.Status.WAITING);
        bookingRepository.save(booking);
        log.trace("Добавлено бронирование ID {}, вещь ID {}.", booking.getItem(), userId);
        return BookingMapper.toBookingDto(booking);
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
        if (!approvedBooking.getStatus().equals(Booking.Status.WAITING)) {
            throw new BadParameterException("Статус бронирования уже изменён.");
        }

        int itemId = approvedBooking.getItem().getId();
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isPresent() && item.get().getOwner().getId() != userId) {
            throw new EntityNotFoundException("Подтердить бронирование может только владелец вещи.");
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
        return BookingMapper.toBookingDto(approvedBooking);
    }

    @Override
    public BookingDtoOut get(int bookingId, int userId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new EntityNotFoundException("Неверно указан ID бронирования.");
        }
        Booking currentBooking = booking.get();
        int bookerId = currentBooking.getBooker().getId();
        Optional<Item> item = itemRepository.findById(currentBooking.getItem().getId());
        if (item.isEmpty()) {
            throw new EntityNotFoundException("Неверно указан ID бронирования.");
        }
        int ownerId = item.get().getOwner().getId();
        if (userId != bookerId & userId != ownerId) {
            throw new EntityNotFoundException("Пользователь не может запрашивать данные.");
        }
        return BookingMapper.toBookingDto(bookingRepository.findBookingById(bookingId));
    }

    @Override
    public List<BookingDtoOut> getAll(int userId, String state, boolean isOwn) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("Указанный пользователь не существует.");
        }
        try {
            StateValues.valueOf(state);
        } catch (IllegalArgumentException ignored) {
            throw new BadParameterException("Unknown state: " + state);
        }
        List<Booking> bookings;
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case "ALL": {
                bookings = isOwn ?
                    bookingRepository.findOwn(userId) :
                    bookingRepository.findBookingsByBookerIdOrderByStartDesc(userId);
                break;
            }
            case "CURRENT": {
                bookings = isOwn ?
                    bookingRepository.findOwnCurrent(userId, now) :
                    bookingRepository.findBookingsByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                            userId, now, now);
                break;
            }
            case "PAST": {
                bookings = isOwn ?
                    bookingRepository.findOwnPast(userId, now) :
                    bookingRepository.findBookingsByBookerIdAndEndIsBeforeOrderByStartDesc(userId, now);
                break;
            }
            case "FUTURE": {
                bookings =  isOwn ?
                    bookingRepository.findOwnFuture(userId, now) :
                    bookingRepository.findBookingsByBookerIdAndStartIsAfterOrderByStartDesc(userId, now);
                break;
            }
            default: {
                bookings =  isOwn ?
                    bookingRepository.findOwnByStatus(userId, Booking.Status.valueOf(state)) :
                    bookingRepository.findBookingsByBookerIdAndStatus(userId, Booking.Status.valueOf(state));
            }
        }
        log.trace("Получено записей бронирования {}.", bookings.size());
        List<BookingDtoOut> bookingDto = new ArrayList<>();
        for (Booking booking : bookings) {
            bookingDto.add(BookingMapper.toBookingDto(booking));
        }
        return bookingDto;
    }

    private enum ApproveValues {
        TRUE, FALSE
    }

    private enum StateValues {
        ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED
    }
}