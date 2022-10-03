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

    public static ItemDtoOutShort itemDtoOutShort(Item item) {
        return new ItemDtoOutShort(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getOwner().getId(),
                item.isAvailable(),
                item.getRequestId()
        );
    }

    public static Item toItem(ItemDto itemDto, User owner) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setOwner(owner);
        item.setAvailable(itemDto.getAvailable());
        Integer requestId = itemDto.getRequestId();
        if (requestId != null) {
            item.setRequestId(itemDto.getRequestId());
        }
        return item;
    }
}