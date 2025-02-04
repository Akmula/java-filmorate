package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public UserDto createUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("POST /users - Запрос на добавление пользователя: {}", userRequest);
        UserDto userDto = userService.createUser(userRequest);
        log.info("POST /films - Ответ на добавление пользователя: {}", userDto);
        return userDto;
    }

    @PutMapping
    public UserDto updateUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("PUT /users - Запрос на обновление пользователя: {}", userRequest);
        UserDto userDto = userService.updateUser(userRequest);
        log.info("PUT /films - Ответ на обновление пользователя: {}", userDto);
        return userDto;
    }

    @DeleteMapping("/{userId}")
    public UserDto deleteUser(@PathVariable int userId) {
        log.info("DELETE /users - Запрос на удаление пользователя: {}", userId);
        return userService.deleteUser(userId);
    }

    @GetMapping
    public Collection<UserDto> getUsers() {
        log.info("GET /users - Запрос на получение пользователей");
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable int userId) {
        log.info("GET /users - Запрос на получение пользователя по id: {}", userId);
        UserDto userDto = userService.getUserById(userId);
        log.info("GET /films - Ответ на получение пользователя по id: {}", userDto);
        return userDto;
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public UserDto addFriend(@PathVariable int userId, @PathVariable int friendId) {
        log.info("PUT /users - Запрос на добавление в друзья пользователя с id: {}," +
                 " от пользователя: {}", friendId, userId);
        return userService.addFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public Collection<UserDto> getFriends(@PathVariable int userId) {
        log.info("GET /users - Запрос на получение друзей пользователя с id: {}", userId);
        return userService.getFriends(userId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public UserDto deleteFriend(@PathVariable int userId, @PathVariable int friendId) {
        log.info("DELETE /users - Запрос на удаление из друзей пользователя с id: {}," +
                 " от пользователя: {}", friendId, userId);
        return userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public Collection<UserDto> getCommonFriends(@PathVariable int userId, @PathVariable int otherId) {
        log.info("GET /users - Запрос на получение общих друзей пользователя с id: {}," +
                 " с пользователем id: {}", userId, otherId);
        return userService.getCommonFriends(userId, otherId);
    }
}