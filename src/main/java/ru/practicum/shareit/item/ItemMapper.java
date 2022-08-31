package ru.practicum.shareit.item;

import ru.practicum.shareit.requests.ItemRequest;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getOwnerId(),
                item.isAvailable(),
                item.getRequestId()
        );
    }

/*    public static List<ItemDto> toItemDto(Iterable<Item> items) {
        List<ItemDto> dtos = new ArrayList<>();
        for (Item item : items) {
            dtos.add(toItemDto(item));
        }
        return dtos;
    }*/

    public static Item toItem(ItemDto itemDto) {
        Item item = new Item(
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable()
        );
        Integer requestId = itemDto.getRequestId();
        if (requestId != null) {
            item.setRequestId(itemDto.getRequestId());
        }
        return item;
    }

}