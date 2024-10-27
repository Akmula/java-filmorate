package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTest {
    UserStorage userStorage;
    UserService userService;
    private UserController userController;

    @BeforeEach
    public void setUp() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        userController = new UserController(userService);
    }

    @Test
    public void whenTheLoginEnteredWithASpaceThrowAnValidationException() {
        User user = User.builder()
                .email("qwerty@qwerty.ru")
                .login("Qwer ty")
                .build();
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void whenTheEmailIncorrectThrowAnValidationException() {
        User user = User.builder()
                .email("qwerty@qwerty")
                .login("Qwerty")
                .build();
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }
}