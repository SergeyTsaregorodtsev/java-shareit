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
    public User addUser(User user) {
        log.trace("Добавлен пользователь {}, ID {}.", user.getName(), user.getId());
        return repository.save(user);
    }

    @Override
    public UserDto updateUser(User user, int userId) {
        log.trace("Изменён пользователь {}, ID {}.", user.getName(), userId);
        return repository.save(user, userId);
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