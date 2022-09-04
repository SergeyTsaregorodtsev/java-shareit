package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, int userId);

    ItemDto updateItem(ItemDto itemDto, int itemId, int userId);

    ItemDtoOut getItem(int itemId, int userId);

    List<ItemDtoOut> getItems(int userId);

    List<ItemDto> search(String text);

    CommentDto addComment(CommentDto comment, int itemId, int userId);

    List<CommentDto> getComments(int itemId);
}
