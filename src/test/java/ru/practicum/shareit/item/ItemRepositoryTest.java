package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired UserRepository userRepository;
    @Autowired ItemRepository itemRepository;
    Item item1, item2;
    User user1, user2;

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(new User(1, "John Doe", "JohnDoe@mail.com"));
        item1 = itemRepository.save(new Item(1, "item1", "item1Desc", user1, true, 1));
        user2 = userRepository.save(new User(2, "Richard Roe", "RichardRoe@mail.com"));
        item2 = itemRepository.save(new Item(2, "item2", "item2Desc", user2, true, 2));
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    void findByOwnerIdOrderById() {
        int userId = user1.getId();
        Page<Item> itemPage = itemRepository.findByOwnerIdOrderById(userId, Pageable.unpaged());
        assertNotNull(itemPage);
        assertEquals(1L, itemPage.getTotalElements());
        assertEquals("item1", itemPage.getContent().get(0).getName());
    }
}