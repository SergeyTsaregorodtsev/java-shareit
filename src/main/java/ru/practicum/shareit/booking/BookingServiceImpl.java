package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto addBooking(BookingDto bookingDto, int userId) {
        int itemId = bookingDto.getId();
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new EntityNotFoundException("Неверно указан ID вещи.");
        }
        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setBooker(userId);
        booking.setStatus(Booking.Status.WAITING);
        bookingRepository.save(booking);
        log.trace("Добавлено бронирование ID {}, вещь ID {}.", booking.getItem(), userId);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public void approve(int bookingId, int userId, String approved) {
        try {
            ApproveStatus.valueOf(approved);
        } catch (IllegalArgumentException ignored) {
            throw new ValidationException("Неверный статус бронирования.");
        }

        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new EntityNotFoundException("Неверно указан ID бронирования.");
        } else {
            int itemId = booking.get().getItem();
            Optional<Item> item = itemRepository.findById(itemId);
            if (item.isPresent() && item.get().getOwnerId() != userId) {
                    throw new ValidationException("Подтердить бронирование может только владелец вещи.");
            }
            Booking.Status status;
            if (approved.equalsIgnoreCase(ApproveStatus.TRUE.name())) {
                status = Booking.Status.APPROVED;
            } else {
                status = Booking.Status.REJECTED;
            }
            booking.get().setStatus(status);
            bookingRepository.save(booking.get());
            log.trace("Статус бронирования ID {} обновлён до {}.", bookingId, status);
        }
    }

    @Override
    public BookingDto get(int bookingId, int userId) {
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
        return BookingMapper.toBookingDto(bookingRepository.findBookingById(bookingId));
    }

    private enum ApproveStatus {
        TRUE,
        FALSE
    }
}
