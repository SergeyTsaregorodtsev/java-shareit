package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;

import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private static Map<Integer, User> users = new HashMap<>();
    static int counter;

    @Override
    public User addUser(UserDto userDto) {
        validateUniqueEmail(userDto,0);
        User user = UserMapper.toUser(userDto);
        user.setId(++counter);
        users.put(counter, user);
        return user;
    }

    @Override
    public User updateUser(UserDto userDto, int userId) {
        validateUniqueEmail(userDto, userId);
        validateUpdate(userDto); // Проверка @NotBlank name (не покрывается аннотациями @Valid для updateUser)
        User user = users.get(userId);

        String name = userDto.getName();
        if (name != null) {
            user.setName(name);
        }
        String email = userDto.getEmail();
        if (email != null) {
            user.setEmail(email);
        }
        users.put(userId, user);
        return user;
    }

    @Override
    public User getUser(int userId) {
        return users.get(userId);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User removeUser(int userId) {
        User user = users.remove(userId);
        if (user == null) {
            throw new UserNotFoundException("Пользователь не найден.");
        }
        return user;
    }

    private void validateUniqueEmail(UserDto userDto, int userId) {
        String email = userDto.getEmail();
        for (User user : users.values()) {
            if (user.getEmail().equalsIgnoreCase(email) & userId != user.getId()) {
                throw new ValidationException("Адрес электронной почты должен быть уникален.");
            }
        }
    }

    private void validateUpdate(UserDto userDto) {
        String name = userDto.getName();
        if (name != null) {
            if (name.isBlank()) {
                throw new ValidationException("Имя пользователя не может быть пустым.");
            }
        }
    }
}
