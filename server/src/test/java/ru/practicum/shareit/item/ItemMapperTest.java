package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingDtoShort;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {
    static User user = new User(1,"John Doe", "john.doe@mail.com");

    @Test
    void toItemDto() {
        Item item = new Item(1, "Hammer", "John Doe's hammer", user,true,0);
        Object object = ItemMapper.toItemDto(item);
        assertEquals(ItemDto.class, object.getClass());
        ItemDto itemDto = (ItemDto) object;
        assertEquals(1, itemDto.getId());
        assertEquals("Hammer", itemDto.getName());
        assertEquals("John Doe's hammer", itemDto.getDescription());
        assertEquals(true, itemDto.getAvailable());
        assertEquals(0, itemDto.getRequestId());
        assertNull(itemDto.getLastBooking());
        assertNull(itemDto.getNextBooking());
    }

    @Test
    void toItem() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        BookingDtoShort lastBooking = new BookingDtoShort(1,1,start,end, Booking.Status.APPROVED);
        ItemDto dto = new ItemDto(1, "Hammer", "John Doe's hammer",true,2,
                lastBooking, null);
        Object object = ItemMapper.toItem(dto, user);
        assertEquals(Item.class, object.getClass());
        Item item = (Item) object;
        assertEquals("Hammer", item.getName());
        assertEquals("John Doe's hammer", item.getDescription());
        assertEquals(user, item.getOwner());
        assertEquals(true, item.isAvailable());
        assertEquals(2, item.getRequestId());
    }
}