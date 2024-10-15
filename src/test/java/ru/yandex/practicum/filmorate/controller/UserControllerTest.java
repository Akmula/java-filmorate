package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest

public class UserControllerTest {
    UserController userController;

    @BeforeEach
    public void setUp() {
        userController = new UserController();
    }

    @Test
    public void whenTheLoginEnteredWithASpaceThrowAnValidationException() {
        User user = User.builder()
                .login("Qwe rty")
                .build();

        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void whenAnInvalidIdIsEnteredThrowAnValidationException() {
        User user = User.builder()
                .email("Qwerty@qwerty.ru")
                .login("Qwerty")
                .build();

        assertThrows(ValidationException.class, () -> userController.updateUser(user));
    }
}