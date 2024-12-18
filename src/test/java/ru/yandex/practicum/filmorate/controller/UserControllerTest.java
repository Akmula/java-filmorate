package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dto.UserRequest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTest {
    UserStorage userStorage;
    UserService userService;
    FriendshipStorage friendshipStorage;
    private UserController userController;

    @BeforeEach
    public void setUp() {

        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage, friendshipStorage);
        userController = new UserController(userService);
    }

    @Test
    public void whenTheLoginEnteredWithASpaceThrowAnValidationException() {
        UserRequest userRequest = UserRequest.builder()
                .email("qwerty@qwerty.ru")
                .login("Qwer ty")
                .build();
        assertThrows(ValidationException.class, () -> userController.createUser(userRequest));
    }

    @Test
    public void whenTheEmailIncorrectThrowAnValidationException() {
        UserRequest userRequest = UserRequest.builder()
                .email("qwerty@qwerty")
                .login("Qwerty")
                .build();
        assertThrows(ValidationException.class, () -> userController.createUser(userRequest));
    }
}