package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.BookingDtoShort;

import java.util.List;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDtoOut {
    final int id;
    final String name;
    final String description;
    final Boolean available;
    final Integer requestId;
    BookingDtoShort lastBooking;
    BookingDtoShort nextBooking;
    List<CommentDto> comments;
}