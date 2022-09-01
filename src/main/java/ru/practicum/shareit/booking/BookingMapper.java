package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.user.*;

@Component
@RequiredArgsConstructor
public class BookingMapper {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public BookingDtoOut toBookingDto(Booking booking) {
        Item item = itemRepository.findById(booking.getItem()).orElseThrow();
        User booker = userRepository.findById(booking.getBooker()).orElseThrow();
        return new BookingDtoOut(
                booking.getId(),
                ItemMapper.toItemDto(item),
                UserMapper.toUserDto(booker),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus()
        );
    }

    public Booking toBooking(BookingDto bookingDto) {
        return new Booking(
                bookingDto.getItemId(),
                bookingDto.getStart(),
                bookingDto.getEnd()
        );
    }
}