package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, int userId);

    ItemDto updateItem(ItemDto itemDto, int itemId, int userId);

    ItemDto getItem(int itemId);

    List<ItemDto> getItems(int userId);

    List<ItemDto> search(String text);
}
