package ru.practicum.shareit.requests;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import ru.practicum.shareit.item.ItemDtoOutShort;

import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ItemRequestDto {
    int id;
    String description;
    int requesterId;
    LocalDateTime created;
    @NonFinal List<ItemDtoOutShort> items;
}