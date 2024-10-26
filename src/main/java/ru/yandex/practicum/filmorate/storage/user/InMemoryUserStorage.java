package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.IdGenerator;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class InMemoryUserStorage implements UserStorage {
    private final IdGenerator idGenerator;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User saveUser(User user) {
        validateUser(user);
        users.put(user.getId(), user);
        log.info("Добавлен пользователь '{}' с id = '{}'", user.getName(), user.getId());
        return user;
    }

    @Override()
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException(user.getId());
        }
        validateUser(user);
        users.put(user.getId(), user);
        log.info("Обновлен пользователь '{}' с id = '{}'", user.getName(), user.getId());
        return user;
    }

    @Override
    public User getUserById(Integer id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(id);
        }
        log.info("Ответ на запрос получения пользователя по id - {}", users.get(id));
        return users.get(id);
    }

    @Override
    public Collection<User> getAllUsers() {
        log.info("Ответ на запрос получения всех пользователей. Кол-во - {}", users.values().size());
        return users.values();
    }

    private void validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы!");
        }
        if (!user.getEmail().contains(".")) {
            throw new ValidationException("Неверно указана почта.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        if (user.getId() == null || user.getId() == 0) {
            user.setId(idGenerator.getNextUserId());
            log.info("Пользователю присвоен id - {}", user.getId());
        }
    }
}