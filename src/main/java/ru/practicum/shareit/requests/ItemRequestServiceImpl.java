package ru.practicum.shareit.requests;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.user.*;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ItemRequestServiceImpl implements ItemRequestService {
    ItemRequestRepository itemRequestRepository;
    ItemRepository itemRepository;
    UserRepository userRepository;

    @Override
    public ItemRequestDto addRequest(ItemRequestDto dto, int userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("ID пользователя указан неверно.");
        }
        ItemRequest itemRequest = itemRequestRepository.save(ItemRequestMapper.toItemRequest(dto, userId));
        log.trace("Добавлен запрос с ID {} от пользователя ID {}.", itemRequest.getId(), userId);
        return ItemRequestMapper.toDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> getOwnRequests(int userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("ID пользователя указан неверно.");
        }
        List<ItemRequest> requests = itemRequestRepository.findAllByRequesterIdOrderByCreated(userId);
        List<ItemRequestDto> dtos = new ArrayList<>();
        for (ItemRequest request : requests) {
            ItemRequestDto dto = ItemRequestMapper.toDto(request);
            addItemsToItemRequestDto(dto);       // Добавляем информацию об ответах
            dtos.add(dto);
        }
        log.trace("Получено {} запросов пользователя ID {}.", dtos.size(), userId);
        return dtos;
    }

    @Override
    public List<ItemRequestDto> getAllRequests(int from, int size, int userId) {
        Sort sortByCreation = Sort.by(Sort.Direction.DESC, "created");
        Pageable page = PageRequest.of(from / size, size, sortByCreation);
        Page<ItemRequest> requestPage = itemRequestRepository.findAll(page);
        List<ItemRequest> itemRequests = requestPage.getContent();
        List<ItemRequestDto> dtos = new ArrayList<>();
        for (ItemRequest request : itemRequests) {
            if (request.getRequesterId() != userId) {
                ItemRequestDto dto = ItemRequestMapper.toDto(request);
                addItemsToItemRequestDto(dto);       // Добавляем информацию об ответах
                dtos.add(dto);
            }
        }
        log.trace("Получено {} запросов других пользователей.", dtos.size());
        return dtos;
    }

    @Override
    public ItemRequestDto get(int requestId, int userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("Неверный ID пользователя.");
        }
        Optional<ItemRequest> request = itemRequestRepository.findById(requestId);
        if (request.isEmpty()) {
            throw new EntityNotFoundException("Неверный ID запроса.");
        }
        ItemRequestDto dto = ItemRequestMapper.toDto(request.get());
        addItemsToItemRequestDto(dto);       // Добавляем информацию об ответах
        log.trace("Получен запрос ID {}.", requestId);
        return dto;
    }

    private void addItemsToItemRequestDto(ItemRequestDto dto) {
        int requestId = dto.getId();
        List<Item> items = itemRepository.findAllByRequestIdOrderById(requestId);
        List<ItemDtoOutShort> itemDtoOutShorts = new ArrayList<>();
        for (Item item : items) {
            itemDtoOutShorts.add(ItemMapper.itemDtoOutShort(item));
        }
        dto.setItems(itemDtoOutShorts);
    }
}