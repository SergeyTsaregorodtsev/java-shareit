package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;

    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") int userId,
                       @Validated({Create.class}) @RequestBody ItemDto itemDto) {
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
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") int userId) {
        log.trace("Получен GET-запрос на получение списка вещей пользователя ID {}.", userId);
        return service.getItems(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto get(@PathVariable int itemId) {
        log.trace("Получен GET-запрос на вещь ID {}.", itemId);
        return service.getItem(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam(name = "text") String text) {
        log.trace("Получен GET-запрос на поиск вещей по ключевому слову '{}'.", text);
        return service.search(text);
    }
}
