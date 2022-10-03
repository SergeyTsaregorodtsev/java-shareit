package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDtoOutShort {
    int id;
    String name;
    String description;
    int ownerId;
    boolean available;
    int requestId;
}
