package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto addItem(ItemDto itemDto, int userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("Неверно указан ID пользователя.");
        }
        Item item = ItemMapper.toItem(itemDto);
        item.setOwnerId(user.get().getId());
        itemRepository.save(item);
        log.trace("Добавлена вещь {}, владелец ID {}.", item.getName(), userId);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, int itemId, int userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("Неверно указан ID пользователя.");
        }
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new EntityNotFoundException("Неверно указан ID вещи.");
        }
        if (item.get().getOwnerId() != userId) {
            throw new EntityNotFoundException("Владелец вещи - другой пользователь.");
        }
        Item updateItem = itemRepository.getReferenceById(itemId);
        String name = itemDto.getName();
        if (name != null) {
            updateItem.setName(name);
        }
        String description = itemDto.getDescription();
        if (description != null) {
            updateItem.setDescription(description);
        }
        Boolean isAvailable = itemDto.getAvailable();
        if (isAvailable != null) {
            updateItem.setAvailable(isAvailable);
        }
        Integer requestId = itemDto.getRequestId();
        if (requestId != null) {
            updateItem.setRequestId(requestId);
        }
        log.trace("Отредактирована вещь ID {}.", itemId);
        itemRepository.save(updateItem);
        return ItemMapper.toItemDto(updateItem);
    }

    @Override
    public ItemDto getItem(int itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new EntityNotFoundException("Неверно указан ID вещи.");
        }
        log.trace("Найдена вещь ID {}.", itemId);
        return ItemMapper.toItemDto(item.get());
    }

    @Override
    public List<ItemDto> getItems(int userId) {
        List<Item> items = itemRepository.findByOwnerId(userId);
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item item : items) {
            itemsDto.add(ItemMapper.toItemDto(item));
        }
        log.trace("Найдено {} вещей, принадлежащих пользователю ID {}.", itemsDto.size(), userId);
        return itemsDto;
    }

    @Override
    public List<ItemDto> search(String text) {
        List<ItemDto> itemsDto = new ArrayList<>();
        if (text.isBlank() || text.length() < 3) {
            return itemsDto;
        }
        List<Item> items = itemRepository.findByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseAndAvailable
                (text, text, true);
        for (Item item : items) {
            itemsDto.add(ItemMapper.toItemDto(item));
        }
        log.trace("Найдено по запросу '{}' вещей: {}.", text, itemsDto.size());
        return itemsDto;
    }
}