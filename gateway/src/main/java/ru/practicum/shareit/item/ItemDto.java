package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.common.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    int id;
    @NotNull(groups = {Create.class})
    @NotBlank(groups = {Create.class})
    String name;
    @NotNull(groups = {Create.class})
    @NotBlank(groups = {Create.class})
    String description;
    @NotNull(groups = {Create.class})
    Boolean available;
    Integer requestId;
    BookingDtoShort lastBooking;
    BookingDtoShort nextBooking;
}
