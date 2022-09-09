package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService service;

    @PostMapping
    public ItemRequestDto addRequest(@RequestHeader("X-Sharer-User-Id") int userId,
                              @RequestBody ItemRequestDto dto) {
        return service.addRequest(dto, userId);
    }

    @GetMapping
    public List<ItemRequestDto> getOwnRequests(@RequestHeader("X-Sharer-User-Id") int userId) {
        return service.getOwnRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests(@RequestParam(name = "from") int from,
                                            @RequestParam(name = "size") int size) {
        return service.getAllRequests(from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto get(@PathVariable int requestId) {
        return service.get(requestId);
    }
}
