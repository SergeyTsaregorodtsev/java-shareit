package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void toUserDto() {
        User user = new User(1, "John Doe", "JohnDoe@mail.com");
        Object object = UserMapper.toUserDto(user);
        assertEquals(UserDto.class, object.getClass());
        UserDto userDto = (UserDto)object;
        assertEquals(1, userDto.getId());
        assertEquals("John Doe", userDto.getName());
        assertEquals("JohnDoe@mail.com", userDto.getEmail());
    }

    @Test
    void toUser() {
        UserDto userDto = new UserDto(1, "John Doe", "JohnDoe@mail.com");
        Object object = UserMapper.toUser(userDto);
        assertEquals(User.class, object.getClass());
        User user = (User)object;
        assertEquals(1, user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("JohnDoe@mail.com", user.getEmail());
    }
}