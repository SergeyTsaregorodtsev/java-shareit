package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {

    User addUser(User user);

    UserDto updateUser(UserDto userDto, int userId);

    UserDto getUser(int userId);

    List<UserDto> getUsers();

    UserDto removeUser(int userId);
}
