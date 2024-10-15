package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    protected Integer userId = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Запрос POST /users - добавить пользователя: {}", user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.debug("Валидация пользователя {}", user);
        validateUser(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Ответ POST /users: {}", user);
        return user;
    }

    @PutMapping()
    public User updateUser(@Valid @RequestBody User newUser) {
        log.info("Запрос PUT /users - обновить пользователя: {}", newUser);
        log.debug("Проверка ID пользователя {}", newUser);
        if (newUser.getId() == null) {
            log.error("Пользователь {} не найден!", (Object) null);
            throw new ValidationException("Id должен быть указан!");
        }
        if (users.containsKey(newUser.getId())) {
            log.debug("Проверка пользователя {}", newUser);
            validateUser(newUser);
            User oldUser = users.get(newUser.getId());

            if (newUser.getName() == null || newUser.getName().isBlank()) {
                newUser.setName(oldUser.getLogin());
            }
            users.put(oldUser.getId(), newUser);
            log.info("Ответ PUT /users - обновленный пользователь: {}", newUser);
            return newUser;
        }
        log.error("Ответ PUT /users - Пользователь с id = {} не найден!", newUser.getId());
        throw new ValidationException("Пользователь с id = " + newUser.getId() + " не найден!");
    }

    @GetMapping
    public Collection<User> getUsers() {
        log.info("GET /users - получение всех пользователей.");
        return users.values();
    }

    private void validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            log.error("Валидация пользователя {} не пройдена", user);
            throw new ValidationException("Логин не может быть пустым и содержать пробелы!");
        }
    }

    private Integer getNextId() {
        return ++userId;
    }
}