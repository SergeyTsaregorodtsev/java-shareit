package ru.practicum.shareit.item;

import java.util.List;

public interface ItemRepository {

    Item addItem(ItemDto itemDto, int ownerId);

    Item updateItem(ItemDto itemDto, int itemId, int ownerId);

    Item getItem(int itemId);

    List<Item> getItems(int userId);

    List<Item> search(String text);
}
