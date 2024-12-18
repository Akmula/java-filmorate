package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.dto.UserRequest;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.Collection;

@Slf4j
@RestController
@ControllerAdvice
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("POST /users - Запрос на добавление пользователя: {}", userRequest);
        UserDto userDto = userService.createUser(userRequest);
        log.info("POST /films - Ответ на добавление пользователя: {}", userDto);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("PUT /users - Запрос на обновление пользователя: {}", userRequest);
        UserDto userDto = userService.updateUser(userRequest);
        log.info("PUT /films - Ответ на обновление пользователя: {}", userDto);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping
    public ResponseEntity<Collection<UserDto>> getUsers() {
        log.info("GET /users - Запрос на получение пользователей");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer userId) {
        log.info("GET /users - Запрос на получение пользователя по id: {}", userId);
        UserDto userDto = userService.getUserById(userId);
        log.info("GET /films - Ответ на получение пользователя по id: {}", userDto);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<UserDto> addFriend(@PathVariable Integer userId, @PathVariable Integer friendId) {
        log.info("PUT /users - Запрос на добавление в друзья пользователя с id: {}," +
                " от пользователя: {}", friendId, userId);
        return ResponseEntity.ok(userService.addFriend(userId, friendId));
    }

    @GetMapping("/{userId}/friends")
    public ResponseEntity<Collection<UserDto>> getFriends(@PathVariable Integer userId) {
        log.info("GET /users - Запрос на получение друзей пользователя с id: {}", userId);
        return ResponseEntity.ok(userService.getFriends(userId));
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<UserDto> deleteFriend(@PathVariable Integer userId, @PathVariable Integer friendId) {
        log.info("DELETE /users - Запрос на удаление из друзей пользователя с id: {}," +
                " от пользователя: {}", friendId, userId);
        return ResponseEntity.ok(userService.deleteFriend(userId, friendId));
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public ResponseEntity<Collection<UserDto>> getCommonFriends(@PathVariable Integer userId, @PathVariable Integer otherId) {
        log.info("GET /users - Запрос на получение общих друзей пользователя с id: {}," +
                " с пользователем id: {}", userId, otherId);
        return ResponseEntity.ok(userService.getCommonFriends(userId, otherId));
    }
}