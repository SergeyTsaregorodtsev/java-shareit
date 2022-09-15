package ru.practicum.shareit.requests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class ItemRequestServiceImplTest {
    ItemRequestService itemRequestService;
    ItemRequestRepository itemRequestRepository;
    ItemRepository itemRepository;
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        itemRequestRepository = mock(ItemRequestRepository.class);
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);
        itemRequestService = new ItemRequestServiceImpl(itemRequestRepository, itemRepository, userRepository);
    }

    @Test
    void addRequest() {
        User user = new User(1, "John Doe", "JohnDoe@mail.com");
        ItemRequestDto itemRequestDto = new ItemRequestDto(1, "In dire need of hammer!",
                1, LocalDateTime.now());
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto,1);
        when(itemRequestRepository.save(any())).thenReturn(itemRequest);
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        ItemRequestDto newItemRequestDto = itemRequestService.addRequest(itemRequestDto, 1);
        verify(itemRequestRepository, times(1)).save(any());
        assertEquals("In dire need of hammer!", newItemRequestDto.getDescription());
    }
}