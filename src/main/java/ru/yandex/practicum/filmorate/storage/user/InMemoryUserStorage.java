package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer userId = 0;

    @Override
    public User saveUser(User user) {
        if (user.getId() == null || user.getId() == 0) {
            user.setId(++userId);
            log.info("Пользователю присвоен id - {}", user.getId());
        }
        users.put(user.getId(), user);
        log.info("Добавлен пользователь '{}' с id = '{}'", user.getName(), user.getId());
        return user;
    }

    @Override()
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException(user.getId());
        }
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
}