package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.user.*;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ItemServiceImpl implements ItemService {
    ItemRepository itemRepository;
    UserRepository userRepository;
    BookingRepository bookingRepository;
    CommentRepository commentRepository;

    @Override
    public ItemDto addItem(ItemDto itemDto, int userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("Неверно указан ID пользователя.");
        }
        Item item = ItemMapper.toItem(itemDto, user.get());
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
        if (item.get().getOwner().getId() != userId) {
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
    public ItemDtoOut getItem(int itemId, int userId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new EntityNotFoundException("Неверно указан ID вещи.");
        }
        log.trace("Найдена вещь ID {}.", itemId);
        ItemDtoOut result = ItemMapper.toItemDtoOut(item.get());
        if (item.get().getOwner().getId() == userId) {
            addBookingToItemDto(result);       // Добавляем информацию о бронировании для владельца
        }
        addCommentsToItemDto(result);      // Добавляем комментарии
        return result;
    }

    @Override
    public List<ItemDtoOut> getItems(int userId, int from, int size) {
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(from / size, size, sortById);
        Page<Item> itemsPage = itemRepository.findByOwnerIdOrderById(userId, page);
        List<Item> items = itemsPage.getContent();
        List<ItemDtoOut> itemsDto = new ArrayList<>();
        for (Item item : items) {
            ItemDtoOut itemDto = ItemMapper.toItemDtoOut(item);
            addBookingToItemDto(itemDto);       // Добавляем информацию о бронировании
            addCommentsToItemDto(itemDto);      // Добавляем комментарии
            itemsDto.add(itemDto);
        }
        log.trace("Найдено {} вещей, принадлежащих пользователю ID {}.", itemsDto.size(), userId);
        return itemsDto;
    }

    @Override
    public List<ItemDto> search(String text, int from, int size) {
        List<ItemDto> itemsDto = new ArrayList<>();
        if (text.isBlank() || text.length() < 3) {
            return itemsDto;
        }
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(from / size, size, sortById);
        Page<Item> itemsPage = itemRepository.findByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseAndAvailable(
                text, text, true, page);
        List<Item> items = itemsPage.getContent();
        for (Item item : items) {
            itemsDto.add(ItemMapper.toItemDto(item));
        }
        log.trace("Найдено по запросу '{}' вещей: {}.", text, itemsDto.size());
        return itemsDto;
    }

    @Override
    public CommentDto addComment(CommentDto commentDto, int itemId, int userId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Item> item = itemRepository.findById(itemId);
        if (user.isEmpty() || item.isEmpty()) {
            throw new EntityNotFoundException("Данные запроса неверны.");
        }
        if (bookingRepository.findBookingsByItemAndBooker(itemId, userId,LocalDateTime.now()).isEmpty()) {
            throw new BadParameterException("Пользователь не пользовался ранее этой вещью.");
        }
        Comment comment = CommentMapper.toComment(commentDto, item.get(), user.get());
        commentRepository.save(comment);
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public List<CommentDto> getComments(int itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new EntityNotFoundException("Неверно указан ID вещи.");
        }
        List<Comment> comments = commentRepository.findCommentsByItemIdOrderByCreated(itemId);
        List<CommentDto> commentsDto = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDto commentDto = CommentMapper.toCommentDto(comment);
            commentsDto.add(commentDto);
        }
        return commentsDto;
    }

    @Override
    public List<CommentDto> getCommentsOwn(int userId) {
        Page<Item> items = itemRepository.findByOwnerIdOrderById(userId, Pageable.unpaged());
        List<CommentDto> commentsDto = new ArrayList<>();
        for (Item item : items.getContent()) {
            List<Comment> comments = commentRepository.findCommentsByItemIdOrderByCreated(item.getId());
            for (Comment comment : comments) {
                commentsDto.add(CommentMapper.toCommentDto(comment));
            }
        }
        return commentsDto;
    }

    private void addBookingToItemDto(ItemDtoOut itemDto) {
        int itemId = itemDto.getId();
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings;
        Booking booking;

        bookings = bookingRepository.findPastBookings(itemId, now);
        if (!bookings.isEmpty()) {
            booking = bookings.get(0);
            itemDto.setLastBooking(BookingMapper.toBookingDtoShort(booking));
        } else {
            itemDto.setLastBooking(null);
        }
        bookings = bookingRepository.findFutureBookings(itemId, now);
        if (!bookings.isEmpty()) {
            booking = bookings.get(0);
            itemDto.setNextBooking(BookingMapper.toBookingDtoShort(booking));
        } else {
            itemDto.setNextBooking(null);
        }
    }

    private void addCommentsToItemDto(ItemDtoOut itemDto) {
        int itemId = itemDto.getId();
        List<Comment> comments = commentRepository.findCommentsByItemIdOrderByCreated(itemId);
        List<CommentDto> commentDtos = new ArrayList<>();
        for (Comment comment : comments) {
            commentDtos.add(CommentMapper.toCommentDto(comment));
        }
        itemDto.setComments(commentDtos);
    }
}