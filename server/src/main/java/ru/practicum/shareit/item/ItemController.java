package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;

    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") int userId,
                       @RequestBody ItemDto itemDto) {
        log.trace("Получен POST-запрос на добавление вещи {} от пользователя ID {}.", itemDto.getName(), userId);
        return service.addItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto,
                          @PathVariable int itemId,
                          @RequestHeader("X-Sharer-User-Id") int userId) {
        log.trace("Получен PATCH-запрос на редактирование вещи {} от пользователя ID {}.", itemDto.getName(), userId);
        return service.updateItem(itemDto, itemId, userId);
    }

    @GetMapping
    public List<ItemDtoOut> getAll(@RequestHeader("X-Sharer-User-Id") int userId,
                                   @RequestParam(name = "from", defaultValue = "0") int from,
                                   @RequestParam(name = "size", defaultValue = "10") int size) {
        log.trace("Получен GET-запрос на получение списка вещей пользователя ID {}.", userId);
        return service.getItems(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemDtoOut getItem(@RequestHeader("X-Sharer-User-Id") int userId,
                                @PathVariable int itemId) {
        log.trace("Получен GET-запрос на вещь ID {}.", itemId);
        return service.getItem(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam(name = "text") String text,
                                @RequestParam(name = "from", defaultValue = "0") int from,
                                @RequestParam(name = "size", defaultValue = "10") int size) {
        log.trace("Получен GET-запрос на поиск вещей по ключевому слову '{}'.", text);
        return service.search(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestBody CommentDto commentDto,
                                 @RequestHeader("X-Sharer-User-Id") int userId,
                                 @PathVariable int itemId) {
        log.trace("Получен POST-запрос на коммент к вещи ID {}.", itemId);
        return service.addComment(commentDto, itemId, userId);
    }

    @GetMapping("/{itemId}/comments")
    public List<CommentDto> getComments(@PathVariable int itemId) {
        return service.getComments(itemId);
    }

    @GetMapping("/comments")
    public List<CommentDto> getCommentsOwn(@RequestHeader("X-Sharer-User-Id") int userId) {
        return service.getCommentsOwn(userId);
    }
}
