package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private static final Map<Integer, Item> items = new HashMap<>();
    private static int counter;

    @Override
    public Item addItem(ItemDto itemDto, int ownerId) {
        Item item = ItemMapper.toItem(itemDto);
        item.setId(++counter);
        item.setOwnerId(ownerId);
        items.put(counter, item);
        return item;
    }

    @Override
    public Item updateItem(ItemDto itemDto, int itemId, int userId) {
        validateUpdate(itemDto, itemId, userId);
        Item item = items.get(itemId);
        String name = itemDto.getName();
        if (name != null) {
            item.setName(name);
        }
        String description = itemDto.getDescription();
        if (description != null) {
            item.setDescription(description);
        }
        Boolean isAvailable = itemDto.getAvailable();
        if (isAvailable != null) {
            item.setAvailable(isAvailable);
        }
        items.put(itemId, item);
        return item;
    }

    @Override
    public Item getItem(int itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> getItems(int userId) {
        List<Item> selectedItems = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwnerId() == userId) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    @Override
    public List<Item> search(String text) {
        List<Item> selectedItems = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.isAvailable()) {
                String name = item.getName().toLowerCase();
                String description = item.getDescription().toLowerCase();
                if (name.contains(text.toLowerCase()) || description.contains(text.toLowerCase())) {
                    selectedItems.add(item);
                }
            }
        }
        return selectedItems;
    }

    private void validateUpdate(ItemDto itemDto, int itemId, int userId) {
        if (items.get(itemId).getOwnerId() != userId) {
            throw new UserNotFoundException("Редактирование вещи, принадлежащей другому пользователю.");
        }
        String name = itemDto.getName();
        if (name != null && name.isBlank()) {
            throw new ValidationException("Наменование вещи не может быть пустым.");
        }
        String description = itemDto.getDescription();
        if (description != null && description.isBlank()) {
            throw new ValidationException("Описание вещи не может быть пустым.");
        }
    }
}