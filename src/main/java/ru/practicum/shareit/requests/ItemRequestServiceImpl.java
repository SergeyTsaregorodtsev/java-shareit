package ru.practicum.shareit.requests;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    @Override
    public ItemRequestDto addRequest(ItemRequestDto dto, int userId) {
        return null;
    }

    @Override
    public List<ItemRequestDto> getOwnRequests(int userId) {
        return null;
    }

    @Override
    public List<ItemRequestDto> getAllRequests(int from, int size) {
        return null;
    }

    @Override
    public ItemRequestDto get(int requestId) {
        return null;
    }
}
