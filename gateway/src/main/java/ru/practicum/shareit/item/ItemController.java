package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.Update;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient client;

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader("X-Sharer-User-Id") int userId,
                                      @Validated({Create.class}) @RequestBody ItemDto itemDto) {
        log.info("Creating item {}", itemDto);
        return client.add(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") int userId,
                                         @PathVariable int itemId,
                                         @Validated({Update.class}) @RequestBody ItemDto itemDto) {
        log.info("Patching itemID {} by item{}", itemId, itemDto);
        return client.update(itemDto, itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") int userId,
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Get items with userId={}, from={}, size={}", userId, from, size);
        return client.getAll(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") int userId,
                                          @PathVariable int itemId) {
        log.info("Get itemId {}", itemId);
        return client.getItem(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam(name = "text") String text,
                                        @RequestParam(name = "from", defaultValue = "0") int from,
                                        @RequestParam(name = "size", defaultValue = "10") int size) {
        log.trace("Получен GET-запрос на поиск вещей по ключевому слову '{}'.", text);
        return client.search(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@Valid @RequestBody CommentDto commentDto,
                                            @RequestHeader("X-Sharer-User-Id") int userId,
                                            @PathVariable int itemId) {
        return client.addComment(commentDto, userId, itemId);
    }

    @GetMapping("/{itemId}/comments")
    public ResponseEntity<Object> getComments(@PathVariable int itemId) {
        return client.getComments(itemId);
    }

    @GetMapping("/comments")
    public ResponseEntity<Object> getCommentsOwn(@RequestHeader("X-Sharer-User-Id") int userId) {
        return client.getCommentsOwn(userId);
    }
}