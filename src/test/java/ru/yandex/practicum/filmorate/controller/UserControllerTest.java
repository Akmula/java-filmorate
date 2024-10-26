package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.IdGenerator;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTest {
    IdGenerator idGeneratorUser;
    UserStorage userStorage;
    private UserController userController;

    @BeforeEach
    public void setUp() {
        idGeneratorUser = new IdGenerator();
        userStorage = new InMemoryUserStorage(idGeneratorUser);
        userController = new UserController();
    }

    @Test
    public void whenTheLoginEnteredWithASpaceThrowAnValidationException() {
        User user = User.builder()
                .email("qwerty@qwerty.ru")
                .login("Qwer ty")
                .build();
        assertThrows(ValidationException.class, () -> userController.createUser(userStorage.saveUser(user)));
    }

    @Test
    public void whenTheEmailIncorrectThrowAnValidationException() {
        User user = User.builder()
                .email("qwerty@qwerty")
                .login("Qwerty")
                .build();
        assertThrows(ValidationException.class, () -> userController.createUser(userStorage.saveUser(user)));
    }
}