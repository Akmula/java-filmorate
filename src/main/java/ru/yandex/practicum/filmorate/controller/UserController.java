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
        User addedUser = userService.saveUser(user);
        log.info("POST /users - Ответ на добавление пользователя. " +
                "Добавлен пользователь '{}' с id = '{}'", user.getName(), user.getId());
        return ResponseEntity.ok(addedUser);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        log.info("PUT /users - Запрос на обновление пользователя: {}", user);
        User updateUser = userService.updateUser(user);
        log.info("PUT /users - Ответ на обновление пользователя '{}' с id = '{}'", user.getName(), user.getId());
        return ResponseEntity.ok(updateUser);
    }

    @GetMapping
    public ResponseEntity<Collection<User>> getUsers() {
        log.info("GET /users - Запрос на получение пользователей");
        log.info("GET /users - Ответ на получение всех пользователей. Кол-во - {}", userService.getAllUsers().size());
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
        log.info("GET /users - Запрос на получение пользователя по id: {}", userId);
        User user = userService.getUserById(userId);
        log.info("GET /users - Ответ на запрос получения пользователя по id - {}", user);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<User> addFriend(@PathVariable Integer userId, @PathVariable Integer friendId) {
        log.info("PUT /users - Запрос на добавление в друзья пользователя с id: {}," +
                " от пользователя: {}", friendId, userId);
        User user = userService.addFriend(userId, friendId);
        log.info("PUT /users - Пользователь: {}, добавил в друзья пользователя с id - {}.", user, friendId);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<User> deleteFriend(@PathVariable Integer userId, @PathVariable Integer friendId) {
        log.info("DELETE /users - Запрос на удаление из друзей пользователя с id: {}," +
                " от пользователя: {}", friendId, userId);
        User user = userService.deleteFriend(userId, friendId);
        log.info("DELETE /users - Пользователь: {}, удалил из друзей пользователя с id - {}.", user, friendId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}/friends")
    public ResponseEntity<List<User>> getFriends(@PathVariable Integer userId) {
        log.info("GET /users - Запрос на получение друзей пользователя с id: {}", userId);
        Integer numberOfUsers = userService.getFriends(userId).size();
        log.info("GET /users - Ответ на запрос получения друзей пользователя с id - {}." +
                " Кол-во друзей - {}", userId, numberOfUsers);
        return ResponseEntity.ok(userService.getFriends(userId));
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriends(@PathVariable Integer userId, @PathVariable Integer otherId) {
        log.info("GET /users - Запрос на получение общих друзей пользователя с id: {}," +
                " с пользователем id: {}", userId, otherId);
        Integer numberOfUsers = userService.getCommonFriends(userId, otherId).size();
        log.info("GET /users - Ответ на запрос получения общих друзей. Кол-во друзей - {}", numberOfUsers);
        return ResponseEntity.ok(userService.getCommonFriends(userId, otherId));
    }
}