package ru.practicum.shareit.user;

import java.util.List;

public interface UserRepository {

    User addUser(UserDto userDto);

    User updateUser(UserDto userDto, int userId);

    User getUser(int userId);

    List<User> getUsers();

    User removeUser(int userId);
}
