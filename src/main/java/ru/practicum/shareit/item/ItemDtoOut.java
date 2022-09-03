package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.BookingDtoShort;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemDtoOut {
    private final int id;

    private final String name;

    private final String description;

    private final Boolean available;

    private final Integer requestId;

    private BookingDtoShort lastBooking;

    private BookingDtoShort nextBooking;

    private List<CommentDto> comments;
}
