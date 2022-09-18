package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, int userId);

    ItemDto updateItem(ItemDto itemDto, int itemId, int userId);

    ItemDtoOut getItem(int itemId, int userId);

    List<ItemDtoOut> getItems(int userId, int from, int size);

    List<ItemDto> search(String text, int from, int size);

    CommentDto addComment(CommentDto comment, int itemId, int userId);

    List<CommentDto> getComments(int itemId);

    List<CommentDto> getCommentsOwn(int userId);
}
