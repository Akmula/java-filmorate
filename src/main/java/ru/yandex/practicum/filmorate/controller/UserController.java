package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@ControllerAdvice
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserStorage userStorage;

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        log.info("POST /users - Запрос на добавление пользователя: {}", user);
        return ResponseEntity.ok(userStorage.saveUser(user));
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        log.info("PUT /users - Запрос на обновление пользователя: {}", user);
        return ResponseEntity.ok(userStorage.updateUser(user));
    }

    @GetMapping
    public ResponseEntity<Collection<User>> getUsers() {
        log.info("GET /users - Запрос на получение пользователей");
        return ResponseEntity.ok(userStorage.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
        log.info("GET /users - Запрос на получение пользователя по id: {}", userId);
        return ResponseEntity.ok(userStorage.getUserById(userId));
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<User> addFriend(@PathVariable Integer userId, @PathVariable Integer friendId) {
        log.info("PUT /users - Запрос на добавление в друзья пользователя с id: {}," +
                " от пользователя: {}", friendId, userId);
        return ResponseEntity.ok(userService.addFriend(userId, friendId));
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<User> deleteFriend(@PathVariable Integer userId, @PathVariable Integer friendId) {
        log.info("DELETE /users - Запрос на удаление из друзей пользователя с id: {}," +
                " от пользователя: {}", friendId, userId);
        return ResponseEntity.ok(userService.deleteFriend(userId, friendId));
    }

    @GetMapping("/{userId}/friends")
    public ResponseEntity<List<User>> getFriends(@PathVariable Integer userId) {
        log.info("GET /users - Запрос на получение друзей пользователя с id: {}", userId);
        return ResponseEntity.ok(userService.getFriends(userId));
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriends(@PathVariable Integer userId, @PathVariable Integer otherId) {
        log.info("GET /users - Запрос на получение общих друзей пользователя с id: {}," +
                " с пользователем id: {}", userId, otherId);
        return ResponseEntity.ok(userService.getCommonFriends(userId, otherId));
    }
}