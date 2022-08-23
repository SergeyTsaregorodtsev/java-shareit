package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = repository.addUser(userDto);
        log.trace("Добавлен пользователь {}, ID {}.", user.getName(), user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto, int userId) {
        User user = repository.updateUser(userDto, userId);
        log.trace("Изменён пользователь {}, ID {}.", user.getName(), userId);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto getUser(int userId) {
        User user = repository.getUser(userId);
        log.trace("Получен пользователь {}, ID {}.", user.getName(), userId);
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> users = repository.getUsers();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            userDtos.add(UserMapper.toUserDto(user));
        }
        log.trace("Получено {} пользователей.", userDtos.size());
        return userDtos;
    }

    @Override
    public UserDto removeUser(int userId) {
        User user = repository.removeUser(userId);
        log.trace("Удалён пользователь {}, ID {}.", user.getName(), userId);
        return UserMapper.toUserDto(user);
    }
}