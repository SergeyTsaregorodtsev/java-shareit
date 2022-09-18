package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    static User user = new User(1, "John Doe", "JohnDoe@mail.com");
    static LocalDateTime start = LocalDateTime.now().plusHours(1);
    static LocalDateTime end = LocalDateTime.now().plusDays(1);


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
        Item item = new Item(1, "Hammer", "John Doe's hammer", user,true,0);

        ItemDto itemDto = new ItemDto(1, "Super hammer", "John Doe's super hammer",
                true,1,null,null);
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(itemRepository.getReferenceById(anyInt())).thenReturn(item);
        Item updatedItem = new Item(1, "Super hammer", "John Doe's super hammer",
                user, true, 1);
        ItemDto updatedItemDto = itemService.updateItem(itemDto,1, 1);

        verify(userRepository, times(1)).findById(1);
        verify(itemRepository, times(1)).findById(1);
        verify(itemRepository, times(1)).getReferenceById(1);
        verify(itemRepository, times(1)).save(updatedItem);
        assertEquals("Super hammer", updatedItemDto.getName());
        assertEquals("John Doe's super hammer", updatedItemDto.getDescription());
    }

    @Test
    void getItem() {
        Item item = new Item(1, "Hammer", "John Doe's hammer", user,true,0);
        Booking pastBooking = new Booking(1, item, user, start, end, Booking.Status.APPROVED);
        Comment comment = new Comment(1, "Very heavy hammer!", item, user, end);

        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        when(bookingRepository.findPastBookings(anyInt(), any()))
                .thenReturn(Collections.singletonList(pastBooking));
        when(bookingRepository.findFutureBookings(anyInt(), any())).thenReturn(Collections.emptyList());
        when(commentRepository.findCommentsByItemIdOrderByCreated(anyInt()))
                .thenReturn(Collections.singletonList(comment));
        ItemDtoOut resultItem = itemService.getItem(1,1);
        assertEquals("Hammer", resultItem.getName());
        assertEquals("John Doe's hammer", resultItem.getDescription());
        assertEquals(start, resultItem.getLastBooking().getStart());
        assertEquals(1, resultItem.getComments().size());
        assertEquals(comment.getText(), resultItem.getComments().get(0).getText());
    }

    @Test
    void getItems() {
        Item item = new Item(1, "Hammer", "John Doe's hammer", user,true,0);
        Booking pastBooking = new Booking(1, item, user, start, end, Booking.Status.APPROVED);
        Comment comment = new Comment(1, "Very heavy hammer!", item, user, end);
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        PageRequest pageRequest = PageRequest.of(5, 10, sortById);
        List<Item> items = Collections.singletonList(item);
        Page<Item> itemPages = new PageImpl<>(items, pageRequest, items.size());
        when(itemRepository.findByOwnerIdOrderById(anyInt(), any(PageRequest.class))).thenReturn(itemPages);
        when(bookingRepository.findPastBookings(anyInt(), any()))
                .thenReturn(Collections.singletonList(pastBooking));
        when(bookingRepository.findFutureBookings(anyInt(), any())).thenReturn(Collections.emptyList());
        when(commentRepository.findCommentsByItemIdOrderByCreated(anyInt()))
                .thenReturn(Collections.singletonList(comment));

        List<ItemDtoOut> resultItems = itemService.getItems(1, 5, 10);
        assertEquals(1, resultItems.size());
        assertEquals("Hammer", resultItems.get(0).getName());
        assertEquals(start, resultItems.get(0).getLastBooking().getStart());
        assertEquals(1, resultItems.get(0).getComments().size());
        assertEquals(comment.getText(), resultItems.get(0).getComments().get(0).getText());
    }

    @Test
    void search() {
        Item item = new Item(1, "Hammer", "John Doe's hammer", user,true,0);
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        PageRequest pageRequest = PageRequest.of(5 / 10, 10, sortById);
        List<Item> items = Collections.singletonList(item);
        Page<Item> itemPages = new PageImpl<>(items, pageRequest, items.size());
        when(itemRepository.findByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseAndAvailable(
                anyString(), anyString(), anyBoolean(), any(PageRequest.class))).thenReturn(itemPages);
        List<ItemDto> result = itemService.search("hammer", 5, 10);
        verify(itemRepository, times(1))
                .findByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseAndAvailable(
                        "hammer", "hammer", true, pageRequest);
        assertEquals(1, result.size());
        assertEquals("Hammer", result.get(0).getName());
    }

    @Test
    void addComment() {
        Item item = new Item(1, "Hammer", "John Doe's hammer", user,true,0);
        CommentDto commentDto = new CommentDto(1, "Very heavy hammer!", "Richard Roe", end);
        Booking booking = new Booking(1, item, user, start, end, Booking.Status.APPROVED);
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(bookingRepository.findBookingsByItemAndBooker(anyInt(), anyInt(),any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(booking));

        CommentDto newComment = itemService.addComment(commentDto,1,1);
        verify(userRepository, times(1)).findById(1);
        verify(itemRepository, times(1)).findById(1);
        verify(bookingRepository, times(1))
                .findBookingsByItemAndBooker(anyInt(), anyInt(), any(LocalDateTime.class));
        verify(commentRepository, times(1)).save(any(Comment.class));
        assertEquals("Very heavy hammer!", newComment.getText());
    }

    @Test
    void getComments() {
        Item item = new Item(1, "Hammer", "John Doe's hammer", user,true,0);
        Comment comment = new Comment("Very heavy hammer!", item, user, end);
        when(itemRepository.findById(anyInt()))
                .thenReturn(Optional.of(item));
        when(commentRepository.findCommentsByItemIdOrderByCreated(anyInt()))
                .thenReturn(Collections.singletonList(comment));

        List<CommentDto> result = itemService.getComments(1);
        verify(itemRepository, times(1)).findById(1);
        verify(commentRepository, times(1)).findCommentsByItemIdOrderByCreated(1);
        assertEquals(1, result.size());
        assertEquals("Very heavy hammer!", result.get(0).getText());
    }

    @Test
    void getCommentsOwn() {
        Item item = new Item(1, "Hammer", "John Doe's hammer", user,true,0);
        Comment comment = new Comment("Very heavy hammer!", item, user, end);

        List<Item> items = Collections.singletonList(item);
        Page<Item> itemPages = new PageImpl<>(items, Pageable.unpaged(), items.size());

        when(itemRepository.findByOwnerIdOrderById(1, Pageable.unpaged()))
                .thenReturn(itemPages);
        when(commentRepository.findCommentsByItemIdOrderByCreated(anyInt()))
                .thenReturn(Collections.singletonList(comment));

        List<CommentDto> result = itemService.getCommentsOwn(1);
        verify(itemRepository, times(1)).findByOwnerIdOrderById(1, Pageable.unpaged());
        verify(commentRepository, times(1)).findCommentsByItemIdOrderByCreated(1);
        assertEquals(1, result.size());
        assertEquals("Very heavy hammer!", result.get(0).getText());
    }
}