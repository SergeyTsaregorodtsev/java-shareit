package ru.practicum.shareit.requests;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto addRequest(ItemRequestDto dto, int userId);

    List<ItemRequestDto> getOwnRequests(int userId);

    List<ItemRequestDto> getAllRequests(int from, int size, int userId);

    ItemRequestDto get(int requestId, int userId);
}
