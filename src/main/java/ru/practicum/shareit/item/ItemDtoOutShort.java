package ru.practicum.shareit.item;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ItemDtoOutShort {
    int id;
    String name;
    String description;
    int ownerId;
    boolean available;
    int requestId;
}