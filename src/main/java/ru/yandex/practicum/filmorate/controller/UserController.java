package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@ControllerAdvice
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        log.info("POST /users - Запрос на добавление пользователя: {}", user);
        User response = userService.saveUser(user);
        log.info("POST /users - Ответ на добавление пользователя. Добавлен пользователь: '{}'", response);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        log.info("PUT /users - Запрос на обновление пользователя: {}", user);
        User response = userService.updateUser(user);
        log.info("PUT /users - Ответ на обновление пользователя. Обновлен пользователь: '{}'", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Collection<User>> getUsers() {
        log.info("GET /users - Запрос на получение пользователей");
        Collection<User> response = userService.getAllUsers();
        log.info("GET /users - Ответ на получение всех пользователей: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
        log.info("GET /users - Запрос на получение пользователя по id: {}", userId);
        User response = userService.getUserById(userId);
        log.info("GET /users - Ответ на запрос получения пользователя по id - {}", response);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<User> addFriend(@PathVariable Integer userId, @PathVariable Integer friendId) {
        log.info("PUT /users - Запрос на добавление в друзья пользователя с id: {}," +
                " от пользователя: {}", friendId, userId);
        User response = userService.addFriend(userId, friendId);
        log.info("PUT /users - Пользователь: {}, добавил в друзья пользователя с id - {}.", response, friendId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<User> deleteFriend(@PathVariable Integer userId, @PathVariable Integer friendId) {
        log.info("DELETE /users - Запрос на удаление из друзей пользователя с id: {}," +
                " от пользователя: {}", friendId, userId);
        User response = userService.deleteFriend(userId, friendId);
        log.info("DELETE /users - Пользователь: {}, удалил из друзей пользователя с id - {}.", response, friendId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/friends")
    public ResponseEntity<List<User>> getFriends(@PathVariable Integer userId) {
        log.info("GET /users - Запрос на получение друзей пользователя с id: {}", userId);
        List<User> response = userService.getFriends(userId);
        log.info("GET /users - Ответ на запрос получения друзей пользователя с id - {}." +
                "Список друзей - {}", userId, response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriends(@PathVariable Integer userId, @PathVariable Integer otherId) {
        log.info("GET /users - Запрос на получение общих друзей пользователя с id: {}," +
                " с пользователем id: {}", userId, otherId);
        List<User> response = userService.getCommonFriends(userId, otherId);
        log.info("GET /users - Ответ на запрос получения общих друзей. Список общих друзей - {}", response);
        return ResponseEntity.ok(response);
    }
}