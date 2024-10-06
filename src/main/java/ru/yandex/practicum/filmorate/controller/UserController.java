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

    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        validateUser(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        if (newUser.getId() == null) {
            throw new ValidationException("Id должен быть указан!");
        }
        if (users.containsKey(newUser.getId())) {
            validateUser(newUser);
            User oldUser = users.get(newUser.getId());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setBirthday(newUser.getBirthday());

            if (newUser.getName() == null || newUser.getName().isBlank()) {
                oldUser.setName(newUser.getLogin());
            } else {
                oldUser.setName(newUser.getName());
            }
            return oldUser;
        }
        throw new ValidationException("Пользователь с id = " + newUser.getId() + " не найден!");
    }

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    private void validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы!");
        }
    }

    private int getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}