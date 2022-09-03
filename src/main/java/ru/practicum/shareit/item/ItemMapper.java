package ru.practicum.shareit.item;

import ru.practicum.shareit.user.User;

import java.util.ArrayList;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getRequestId(),
                null,
                null
        );
    }

    public static ItemDtoOut toItemDtoOut(Item item) {
        return new ItemDtoOut(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getRequestId(),
                null,
                null,
                new ArrayList<>()
        );
    }

    public static Item toItem(ItemDto itemDto, User owner) {
        Item item = new Item(
                itemDto.getName(),
                itemDto.getDescription(),
                owner,
                itemDto.getAvailable()
        );
        Integer requestId = itemDto.getRequestId();
        if (requestId != null) {
            item.setRequestId(itemDto.getRequestId());
        }
        return item;
    }
}