package ru.practicum.shareit.requests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class ItemRequestServiceImplTest {
    ItemRequestRepository itemRequestRepository = mock(ItemRequestRepository.class);
    ItemRepository itemRepository = mock(ItemRepository.class);
    UserRepository userRepository = mock(UserRepository.class);
    ItemRequestService service = new ItemRequestServiceImpl(itemRequestRepository, itemRepository, userRepository);
    User user = new User(1, "John Doe", "JohnDoe@mail.com");
    ItemRequest request = new ItemRequest(1, "In dire need of hammer!", 1, LocalDateTime.now());
    Item item = new Item(1, "Hammer", "John Doe's hammer",user, true,1);

    @BeforeEach
    void setUp() {
    }

    @Test
    void addRequest() {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1, "In dire need of hammer!",
                1, LocalDateTime.now());
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto,1);
        when(itemRequestRepository.save(any())).thenReturn(itemRequest);
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        ItemRequestDto newItemRequestDto = service.addRequest(itemRequestDto, 1);
        verify(itemRequestRepository, times(1)).save(any());
        assertEquals("In dire need of hammer!", newItemRequestDto.getDescription());
    }

    @Test
    void get() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(anyInt())).thenReturn(Optional.of(request));
        when(itemRepository.findAllByRequestIdOrderById(anyInt())).thenReturn(Collections.singletonList(item));

        ItemRequestDto result = service.get(1,1);
        verify(userRepository, times(1)).findById(1);
        verify(itemRequestRepository, times(1)).findById(1);
        verify(itemRepository, times(1)).findAllByRequestIdOrderById(1);
        assertEquals("In dire need of hammer!", result.getDescription());
        assertEquals(1, result.getItems().size());
        assertEquals("Hammer", result.getItems().get(0).getName());
    }

    @Test
    void getOwnRequests() {
        List<ItemRequest> requests = Collections.singletonList(request);
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findAllByRequesterIdOrderByCreated(anyInt())).thenReturn(requests);
        when(itemRepository.findAllByRequestIdOrderById(anyInt())).thenReturn(Collections.singletonList(item));

        List<ItemRequestDto> result = service.getOwnRequests(1);
        verify(itemRequestRepository, times(1)).findAllByRequesterIdOrderByCreated(1);
        assertEquals(1, result.size());
        assertEquals("In dire need of hammer!", result.get(0).getDescription());
        assertEquals(1, result.get(0).getItems().size());
        assertEquals("Hammer", result.get(0).getItems().get(0).getName());
    }

    @Test
    void getAllRequests() {
        List<ItemRequest> requests = new ArrayList<>();
        requests.add(request);
        ItemRequest request2 = new ItemRequest(2, "In dire need of hammer too!", 2, LocalDateTime.now());
        requests.add(request2);
        Sort sortByCreation = Sort.by(Sort.Direction.DESC, "created");
        Pageable page = PageRequest.of(5 / 10, 10, sortByCreation);
        Page<ItemRequest> requestPage = new PageImpl<>(requests, page, requests.size());
        when(itemRequestRepository.findAll(any(PageRequest.class))).thenReturn(requestPage);
        when(itemRepository.findAllByRequestIdOrderById(anyInt())).thenReturn(Collections.singletonList(item));

        List<ItemRequestDto> result = service.getAllRequests(5,10,1);
        verify(itemRequestRepository, times(1)).findAll(page);
        assertEquals(1, result.size());
        assertEquals("In dire need of hammer too!", result.get(0).getDescription());
        assertEquals(1, result.get(0).getItems().size());
        assertEquals("Hammer", result.get(0).getItems().get(0).getName());
    }
}