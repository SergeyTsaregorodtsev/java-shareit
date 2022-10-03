package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto);

    UserDto updateUser(UserDto user, int userId);

    UserDto getUser(int userId);

    List<UserDto> getUsers();

    UserDto removeUser(int userId);
}
