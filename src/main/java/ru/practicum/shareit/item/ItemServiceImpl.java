package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto addItem(ItemDto itemDto, int userId) {
        if (userRepository.getUser(userId) == null) {
            throw new UserNotFoundException("Владелец вещи не найден.");
        }
        Item item = itemRepository.addItem(itemDto, userId);
        log.trace("Добавлена вещь {}, владелец ID {}.", item.getName(), userId);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, int itemId, int userId) {
        Item item = itemRepository.updateItem(itemDto, itemId, userId);
        log.trace("Отредактирована вещь ID {}.", itemId);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto getItem(int itemId) {
        Item item = itemRepository.getItem(itemId);
        log.trace("Найдена вещь ID {}.", itemId);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getItems(int userId) {
        List<ItemDto> items = new ArrayList<>();
        for (Item item : itemRepository.getItems(userId)) {
            items.add(ItemMapper.toItemDto(item));
        }
        log.trace("Найдено {} вещей, принадлежащих пользователю ID {}.", items.size(), userId);
        return items;
    }

    @Override
    public List<ItemDto> search(String text) {
        List<ItemDto> items = new ArrayList<>();
        if (!text.isEmpty()) {
            for (Item item : itemRepository.search(text)) {
                items.add(ItemMapper.toItemDto(item));
            }
        }
        log.trace("Найдено по запросу '{}' вещей: {}.", text, items.size());
        return items;
    }
}