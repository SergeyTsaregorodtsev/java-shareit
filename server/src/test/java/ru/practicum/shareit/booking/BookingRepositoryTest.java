package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {
    @Autowired UserRepository userRepository;
    @Autowired ItemRepository itemRepository;
    @Autowired BookingRepository bookingRepository;
    User user1, user2;
    Item item1, item2;
    Booking booking1, booking2;
    LocalDateTime start = LocalDateTime.now().plusHours(1);
    LocalDateTime end = LocalDateTime.now().plusDays(1);

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(new User(1, "John Doe", "JohnDoe@mail.com"));
        user2 = userRepository.save(new User(2, "Richard Roe", "RichardRoe@mail.com"));
        item1 = itemRepository.save(new Item(1, "item1", "item1Desc", user1, true, 0));
        item2 = itemRepository.save(new Item(2, "item2", "item2Desc", user2, true, 0));
        booking1 = bookingRepository.save(new Booking(1, item1, user1, start, end, Booking.Status.WAITING));
        booking2 = bookingRepository.save(new Booking(2, item2, user2, start, end, Booking.Status.WAITING));
    }

    @AfterEach
    void tearDown() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findOwn() {
        int userId = user1.getId();
        Page<Booking> bookingPage = bookingRepository.findOwn(userId, Pageable.unpaged());
        assertNotNull(bookingPage);
        assertEquals(1L, bookingPage.getTotalElements());
        assertEquals("John Doe", bookingPage.getContent().get(0).getItem().getOwner().getName());
    }

    @Test
    void findBookingsByItemAndBooker() {
        int userId = user2.getId();
        int itemId = item2.getId();
        List<Booking> bookings = bookingRepository.findBookingsByItemAndBooker(itemId, userId, LocalDateTime.now().plusDays(2));
        assertEquals(1, bookings.size());
        assertEquals("Richard Roe", bookings.get(0).getBooker().getName());
    }
}