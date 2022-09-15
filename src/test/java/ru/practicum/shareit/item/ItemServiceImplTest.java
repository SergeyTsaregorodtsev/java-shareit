package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class ItemServiceImplTest {
    ItemRepository itemRepository;
    UserRepository userRepository;
    BookingRepository bookingRepository;
    CommentRepository commentRepository;
    ItemService itemService;

    @BeforeEach
    void setUp() {
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);
        bookingRepository = mock(BookingRepository.class);
        commentRepository = mock(CommentRepository.class);
        itemService = new ItemServiceImpl(itemRepository, userRepository, bookingRepository, commentRepository);
    }

    @Test
    void addItem() {
        User user = new User(1, "John Doe", "JohnDoe@mail.com");
        ItemDto itemDto = new ItemDto(1, "Hammer", "John Doe's hammer",
                true,null,null,null);
        Item item = ItemMapper.toItem(itemDto, user);
        when(itemRepository.save(any())).thenReturn(item);
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        ItemDto newItemDto = itemService.addItem(itemDto, 1);
        verify(itemRepository, times(1)).save(item);
        assertEquals("Hammer", newItemDto.getName());
    }

    @Test
    void updateItem() {
    }

    @Test
    void getItem() {
    }

    @Test
    void getItems() {
    }
}