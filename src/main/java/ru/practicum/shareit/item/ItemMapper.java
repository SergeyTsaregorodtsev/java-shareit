package ru.practicum.shareit.item;

import ru.practicum.shareit.requests.ItemRequest;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getOwnerId(),
                item.isAvailable(),
                item.getRequest() != null ? item.getRequest() : null
        );
    }

    public static Item toItem(ItemDto itemDto) {
        Item item = new Item(
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable()
        );
        ItemRequest request = itemDto.getRequest();
        if (request != null) {
            item.setRequest(request);
        }
        return item;
    }
}