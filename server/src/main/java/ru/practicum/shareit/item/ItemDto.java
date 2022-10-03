package ru.practicum.shareit.item;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.BookingDtoShort;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class ItemDto {
    int id;
    String name;
    String description;
    Boolean available;
    Integer requestId;
    BookingDtoShort lastBooking;
    BookingDtoShort nextBooking;
}